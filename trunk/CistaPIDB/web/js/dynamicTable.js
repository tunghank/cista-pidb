var MAX_ROW_PER_TABLE = 100;
var MAX_CELL_PER_ROW = 100;
var MAX_RND_NAME = 10;
var rowSeparator = "\r\n";
var cellSeparator = ",\t";
function deleteRow(obj, confirmMsg) {
	if(confirmMsg) {
		if(!confirm(confirmMsg)) {
			return;
		}
	}
	var thisTable;
	var thisTr;
	thisTr = findNestTr(obj);
	if(thisTr!=null) {
		thisTable = findNestTable(thisTr);
	}
	if(thisTr==null || thisTable==null) {
		return;
	}

	//at least 1 header, 1 footer, 1 body = 3 rows left.
	//if(thisTable.rows.length>2) {
	thisTable.deleteRow(findRowIndex(thisTable, thisTr));
	//}
}

//tableId: the table's id
//template: the temlate for the row.
function newRow(tableId, template) {
	var thisTable = $(tableId);

	var tableId;
	if(thisTable.id) {
		tableId = thisTable.id;
	} else {
		tableId = "T"+Math.floor(Math.random() * 100);
	}
	

	var newTr = thisTable.insertRow(thisTable.rows.length-1);
	var iter = 0;
	var newTrId = tableId+"_R"+iter;
	//Generate new row id.
	while($(newTrId)) {
		if(iter==MAX_ROW_PER_TABLE) {
			alert("Max row "+MAX_ROW_PER_TABLE+" reached, add new row fail!");
			return false;
		}
		iter++;
		newTrId = tableId+"_R"+iter;		
	}
	newTr.id = newTrId;

	iter = 0;
	for(var i=0; i<template.length; i++) {
		var newCell = newTr.insertCell();
		var newCellId = newTrId+"_C"+i;
		newCell.id = newCellId;
//		if(aboveTr.cells[i].getAttribute("COLSPAN")) {
//			newCell.colSpan = aboveTr.cells[i].getAttribute("COLSPAN");
//		}
		var cellContent = template[i];
		while(cellContent.indexOf("#ID#")>=0) {
			cellContent = cellContent.replace("#ID#", newTrId+"_V"+iter);
			iter ++;
		}

		//Support 10 random names
		for(var j=1; j<MAX_RND_NAME+1; j++) {
			var rndName = "RND" + j + "_" + Math.floor(Math.random() * 1000000);
			while(cellContent.indexOf("#RND"+j+"#")>=0) {
				cellContent = cellContent.replace("#RND"+j+"#", rndName);
			}
		}
		
		newCell.innerHTML = cellContent;		
	}
	
	return newTrId;
}

function findNestTr(obj) {
	var pObj = obj.parentNode;
	var thisTr;
	//try 5 levels to find the nest table
	for(var i=0; i<5; i++) {
		if(pObj.tagName.toLowerCase()=='tr') {
			thisTr = pObj;
			break;
		} else {
			pObj = pObj.parentNode;
		}
	}
	return thisTr;
}

function findNestTable(obj) {
	var pObj = obj.parentNode;
	var thisTable;
	//try 5 levels to find the nest table
	for(var i=0; i<5; i++) {
		if(pObj.tagName.toLowerCase()=='table') {
			thisTable = pObj;
			break;
		} else {
			pObj = pObj.parentNode;
		}
	}
	return thisTable;
}

function findRowIndex(table, tr) {
	for(var i=0; i<table.rows.length; i++) {
		if(table.rows[i]==tr) {
			return i;
		}	
	}
	return -1;
}

//src: source table id
//dest: dest hidden field id
//ih: ignore header row count default=1
//ib: ignore bottom row count default=1
function packData(src, dest, ih, ib) {
	var packedData = "";
	if(!ih && ih!=0) ih=1;
	if(!ib && ib!=0) ib=1;
	var srcTable = $(src);
	var destField = $(dest);
	
	if(!srcTable || !destField) {
		alert("ERROR: no source table or dest field defined!");
		return false;
	}
	
	var rows = srcTable.rows;
	for(var i=ih; i<rows.length-ib; i++) {
		var row = rows[i];
		var rowId = row.id;
		var j=0;
		if(i!=ih) {
			packedData += rowSeparator;
		}
		while($(rowId+"_V"+j)) {
			if(j>MAX_CELL_PER_ROW) {
				alert("ERROR: out of max cell defined "+MAX_CELL_PER_ROW+"!");
			}
			if(j!=0) {
				packedData += cellSeparator;
			}
			packedData += escape($F(rowId+"_V"+j));
			j++;
		}
	}
	destField.value = escape(packedData);
}

//src: dest hidden field id
//dest: source table id
function unpackData(src, dest, template) {
	var rows = unescape($(src).value).split(rowSeparator);

	if(rows!='' && rows.length>0) {
		for(var i=0; i<rows.length; i++) {
			var newRowId = newRow(dest, template);
			var values = rows[i].split(cellSeparator);
			if(values!='' && values.length>0) {
				for(var j=0; j<values.length; j++) {
					var obj = $(newRowId+"_V"+j);
					if(obj) {
						obj.value = unescape(values[j]);
					}
				}
			}
		}
	}
	
}
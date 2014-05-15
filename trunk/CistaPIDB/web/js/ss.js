SmartSearch = function() {
}

/**
 *   Smart Search Setup:
 *   param. name   | description
 *  -------------Required------------------------------------------------------------------------------
 *   button        | The smart search button's id. (Required)
 *   inputField    | Input text field. (Required)
 *   cp            | Web application context path. (Required)
 *  -------------Optional(Must)------------------------------------------------------------------------ 
 *   name          | The pre-defined smart search name. Mapping to the name defined in SmartSearch.xml. (Required if 'table' not specified)
 *                 |
 *   table         | The database table name for query. (Required if 'name' not specified)
 *   keyColumn     | The table's column name for query. (Required if 'table' is specified)
 *   columns       | The columns for displaying (returning), split by comma (,). (Required if 'table' is specified)
 *  -------------Optional------------------------------------------------------------------------------ 
 *   title         | The title displayed on smart search dialog.
 *   mode          | Special smart search mode: 0=single select, 1=multi select.
 *   whereCause    | The optional sql where cause statment. Support parameters ex: USER_NAME = {userName} and ACTIVE = 1.
 *   orderBy       | The order column name. Default order by key column.
 *   callbackHandle| The javascript function name for smart search call back, when selection completed in smart search dialog.
 *   autoSearch    | Whether to carry parameter in text field, and execute auto search. true or false. Usually true on query page, false on create/edit page.
 *   width         | The dialog width.
 *   height        | The dialog height. 
 *
*/
SmartSearch.setup = function (params) {
	function param_default(pname, def) { if (typeof params[pname] == "undefined") { params[pname] = def; }};
	param_default("title", null);
	param_default("button", null);
	param_default("cp", "/");
	param_default("inputField", null);
	param_default("name", null);
	param_default("table", null);
	param_default("keyColumn", null);
	param_default("columns", null);
	param_default("whereCause", null);
	param_default("orderBy", null);
	param_default("mode", null);
	param_default("callbackHandle", "SSDefaultCallbackHandle");
	param_default("autoSearch", true);
	param_default("width", 600);
	param_default("height", 400);
	if (typeof params["button"] == "string") {
		params["button"] = document.getElementById(params["button"]);
	}

	if (params.button == null || params.inputField == null || params.cp == null || (params.name==null && !(params.table!=null && params.keyColumn!=null))) {
		alert("Smart search setup:\n  no button or smart search found.  Please check your code");
		return false;
	}

	params.button["onclick"] = function() {
		var width = params.width;
		var height = params.height;
		var left = (screen.width-width)/2;
		var top = (screen.height-height)/2;
		var target = params.cp + "/dialog/smart_search.do?m=search";
		target += "&inputField=" + params.inputField;
		
		if(params.autoSearch) {
			target += "&inputFieldValue=" + $F(params.inputField);
		}
		
		if (params.name != null) {
			target += "&name=" + params.name;
		}

		if (params.table != null) {
			target += "&table=" + params.table;
		}

		if (params.keyColumn != null) {
			target += "&keyColumn=" + params.keyColumn;
		}

		if (params.columns != null) {
			target += "&columns=" + params.columns;
		}

		if (params.whereCause != null) {
			target += "&whereCause=" + SSParseWhereCause(params.whereCause);
		}

		if (params.orderBy != null) {
			target += "&orderBy=" + params.orderBy;
		}

		if (params.mode != null) {
			target += "&mode=" + params.mode;
		}

		if (params.callbackHandle != null) {
			target += "&callbackHandle=" + params.callbackHandle;
		}
		
		if (params.title != null) {
			target += "&title=" + params.title;
		}
		var x = window.open(target,"smart_search","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
		x.focus();
	}
};

function onSSSelect() {
	if (vHandle!="") {
		var selected = new Array();
		var allItem = document.getElementsByName("item");
		if (allItem && allItem.length > 0) {
			for (var i=0; i<allItem.length; i++) {
				if (allItem[i].checked) {
					var tr = findNestTr(allItem[i]);
					var itemValue = {};
					for (var j=1; j<tr.cells.length; j++) {
						itemValue[vColumns[j-1]] = tr.cells[j].innerText;
					}
					selected.push(itemValue);
				}
			}
		}
		if (selected.length > 0) {
			eval("window.opener."+vHandle+"(vInputField, vColumns, selected)");
		}
	}
	window.close();
}


function SSDefaultCallbackHandle(inputField, columns, value) {
	if ($(inputField) && value != null && value.length > 0) {
		var tempValue = "";

		for(var i = 0; i < value.length; i++) {
			tempValue += "," + value[i][columns[0]];
		}

		if(tempValue != "") {
			$(inputField).value = tempValue.substring(1);
		}
	}
}

function SSSelectCallbackHandle(selectField, columns, value) {
	if ($(selectField) && value != null && value.length > 0) {
		removeOptions($(selectField), 0);
		for(var i = 0; i < value.length; i++) {
			addOption($(selectField), value[i][columns[0]], value[i][columns[0]]);
		}
	}
}

function SSParseWhereCause(whereCause) {
	var startIdx = 0;
	while(whereCause.indexOf("{", startIdx)>=0) {
		var leftBracketIdx = whereCause.indexOf("{", startIdx);
		var rightBracketIdx = whereCause.indexOf("}", startIdx + 1);
		if (rightBracketIdx < 0) {
			break;
		}
		
		var varName = whereCause.substring(leftBracketIdx + 1, rightBracketIdx);
		var newWhereCause = whereCause.substring(0, leftBracketIdx);
		if (whereCause.substring(leftBracketIdx-1, leftBracketIdx) != "'") {
			newWhereCause = newWhereCause + "'";
		}
		newWhereCause += $F(varName);
		if (whereCause.substring(rightBracketIdx+1, rightBracketIdx+2) != "'") {
			newWhereCause = newWhereCause + "'";
		}
		startIdx = newWhereCause.length + 1;
		newWhereCause += whereCause.substring(rightBracketIdx + 1);

		whereCause = newWhereCause;
	}

	if (whereCause.substring(0, 1) == " ") {
		whereCause = whereCause.substring(1);
	}

	if (whereCause.substring(0, 4).toLowerCase() == "and ") {
		whereCause = whereCause.substring(4);
	}
	
	return whereCause;
}

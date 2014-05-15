function removeOptions(sel, keepFirst) {
	var keepCount = 0;
	if(keepFirst) {
		keepCount = 1;
	}
	
	while(sel.options.length>keepCount) {
		sel.options.remove(keepCount);
	}
}

function removeOption(sel, keepFirst) {

	sel.options.remove(keepFirst);

}

function removeSelectedOptions(sel) {
	while(sel.selectedIndex != -1) {
		sel.options.remove(sel.selectedIndex);
	}
}

function addOption(sel, text, value) {
	var oOption = document.createElement("OPTION");
	oOption.text=text;
	oOption.value=value;
	sel.add(oOption);
}

function addDifferOption(sel, text, value) {
	var oOption = document.createElement("OPTION");
	oOption.text=text;
	oOption.value=value;
	var len = sel.options.length;
	var flag = false;
	for (i = 0; i < len; i++) {
		if (sel.options[i].value == value) {
			flag = true;
			break;
		}
	}
	if (!flag) {
		sel.add(oOption);
	}
	
}

function selectAllOptions(sel) {
	if (sel && sel.options) {
		for(var i=0; i<sel.options.length; i++) {
			sel.options[i].selected = true;
		}
	}
}

function autoFitBottomArea(area, buttomMargin, fixHeight) {
	if ($(area)) {
		var p = $(area).parentNode;
		$(area).style.width = p.clientWidth;
		var height = screen.availHeight - p.getClientRects()[0].top;
		if (fixHeight) {
			height = fixHeight - p.getClientRects()[0].top;
		}
		if (buttomMargin) {
			height = height - buttomMargin;
		}
		$(area).style.height = height;
	}
}

function setMessage(target, msg) {
	if ($(target)) {
		$(target).innerHTML = msg;
	}
}

function showHidePanel(panel) {
	if ($(panel)) {
		if ($(panel).style.display=="" || $(panel).style.display=="block") {
			$(panel).style.display = "none";
		} else {
			$(panel).style.display = "block";
		}
	}
}

//paging functions
//paging functions for multi-paging
function prevPage(n) {
	if (n) {
		$("currentTab").value=n;
		$("pageNo" + n).value = parseInt($("pageNo" + n).value) - 1;
		$("pagingForm").action = $("pagingForm").action + "?m=paging";
		$("pagingForm").submit();
	} else {
		$("pageNo").value = parseInt($("pageNo").value) - 1;
		$("pagingForm").action = $("pagingForm").action + "?m=paging";
		$("pagingForm").submit();
	}
}

function nextPage(n) {
	if (n) {
		$("currentTab").value=n;
		$("pageNo" + n).value = parseInt($("pageNo" + n).value) + 1;
		$("pagingForm").action = $("pagingForm").action + "?m=paging";
		$("pagingForm").submit();
	} else {
		$("pageNo").value = parseInt($("pageNo").value) + 1;
		$("pagingForm").action = $("pagingForm").action + "?m=paging";
		$("pagingForm").submit();
	}
}

function reportDownload(fm, act) {
	if ($(fm)) {
		var oldAction = $(fm).action;
		if (act) {
			$(fm).action = act;
		} else {
			$(fm).action = $(fm).action + "?m=download";
		}
		var tempTarget = $(fm).target;
		$(fm).target = "_top";
		$(fm).submit();
		$(fm).action = oldAction;
		$(fm).target = tempTarget;
	}
}

function getRadioValue(r) {
	var radios = document.getElementsByName(r);
	for (var i=0; i<radios.length; i++) {
		if (radios[i].checked) {
			return radios[i].value;
		}
	}
	return null;
}

 //BY Value
function SelectItem_Combo_Value(objList, szItem)
{
	var nIndex;
	
	if (objList.length == 0) {
		return;
	}

	//----- 1. search the szItem in objList.options[]
	nIndex = -1;
	for (i=0 ; i<objList.length; i++)
	{
		if (objList.options[i].value == szItem) {
			nIndex = i;
		}
	}

	//----- 2. check if find or not
	if (nIndex >= 0) {
		objList.selectedIndex = nIndex;
	}
} //--- end SelectItem_Combo () ---
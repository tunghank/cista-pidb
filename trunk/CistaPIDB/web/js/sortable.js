/**
 * Usage: add onclick event on table header(td) parameters: this: always write
 * this. ih: ignore header lines. columnIdx: column index which to
 * sort.(optional) PS: Please keep table in following hierarchy: <table>
 * <tr>
 * <td onclick="sort(this, 1)"></td>
 * ...</tr>
 * ....
 * <tr>
 * <td></td>
 * ...</tr>
 * </table>
 */
var oddColor = "ASE-TableContent01";
var evenColor = "ASE-TableContent02";
function sort(arg, ih) {
	var startTime = (new Date()).getTime();
	var thisTR = arg.parentNode;
	var thisTable = arg.parentNode.parentNode.parentNode;
	var allTR = thisTable.rows;

	var columnIdx = 0;
	var data = new Array();

	for (var i = ih; i < allTR.length; i++) {
		data.push(allTR[i]);
	}

	if (data.length == 0) {
		return;
	}

	// member old color
	if (data.length > 1) {
		oddColor = data[0].cells(0).className;
		evenColor = data[1].cells(0).className;
	}

	// find sort column index.
	if (arguments.length == 3) {
		columnIdx = arguments[2] - 1;
	} else {
		for (i = 0; i < thisTR.cells.length; i++) {
			if (thisTR.cells[i] == arg) {
				columnIdx = i;
				break;
			}
		}
	}

	// do sort increase
	// doIncrease(allTR, columnIdx, ih, allTR.length-1);
	quicksortIncrease(allTR, columnIdx, ih, allTR.length - 1);
	// end sort

	// if order not changed after sort, try sort as decrease.
	var sortDecrease = true;
	for (var j = 0; j < data.length; j++) {
		if (data[j] != allTR[j + ih]) {
			sortDecrease = false;
			break;
		}
	}

	if (sortDecrease) {
		// do sort decrease
		// doDecrease(allTR, columnIdx, ih, allTR.length-1);
		quicksortDecrease(allTR, columnIdx, ih, allTR.length - 1);
		// end sort
	}

	reColor(thisTable, ih);
	var endTime = (new Date()).getTime();
	// window.status="cast "+(endTime-startTime)+"ms";
}

// reset color
function reColor(table, ih) {
	// alert(evenColor+":"+oddColor);
	var rows = table.rows;
	var c = oddColor;
	for (var i = ih; i < rows.length; i++) {
		if ((i - ih) % 2 == 0) {
			c = oddColor;
		} else {
			c = evenColor;
		}
		for (var j = 0; j < rows[i].cells.length; j++) {
			rows[i].cells(j).className = c;
		}
	}
}

// if x > y, return 1, x==y, return 0, x<y return -1
function comp(x, y) {
	if (!isNaN(x) && !isNaN(y)) {
		var n = Number(x);
		var m = Number(y);
		if (n > m)
			return 1;
		if (n < m)
			return -1;
		return 0;
	} else if (isValidDate(x) && isValidDate(y)) {
		return compareDate(x, y);
	} else if (isValidTime(x) && isValidTime(y)) {
		return compareTime(x, y);
	} else if (isValidDateTime(x) && isValidDateTime(y)) {
		return compareDateTime(x, y);
	} else {
		if (x > y)
			return 1;
		if (x < y)
			return -1;
		return 0;
	}
}

function quicksortIncrease(s, c, l, r) {
	if (l >= r)
		return;
	var i = l;
	var j = r + 1;
	var pivot = l;
	while (true) {
		do {
			i++;
		} while (i <= r
				&& comp(s[i].cells[c].innerText, s[pivot].cells[c].innerText) > 0)

		do {
			j--;
		} while (j >= l
				&& comp(s[j].cells[c].innerText, s[pivot].cells[c].innerText) < 0)

		if (i >= j)
			break;
		if (comp(s[i].cells[c].innerText, s[j].cells[c].innerText) != 0)
			s[i].swapNode(s[j]);
	}
	if (comp(s[j].cells[c].innerText, s[pivot].cells[c].innerText) != 0)
		s[j].swapNode(s[pivot]);
	quicksortIncrease(s, c, l, j - 1);
	quicksortIncrease(s, c, j + 1, r);
}

function quicksortDecrease(s, c, l, r) {
	if (l >= r)
		return;
	var i = l;
	var j = r + 1;
	var pivot = l;
	while (true) {
		do {
			i++;
		} while (i <= r
				&& comp(s[i].cells[c].innerText, s[pivot].cells[c].innerText) < 0)

		do {
			j--;
		} while (j >= l
				&& comp(s[j].cells[c].innerText, s[pivot].cells[c].innerText) > 0)

		if (i >= j)
			break;
		if (comp(s[i].cells[c].innerText, s[j].cells[c].innerText) != 0)
			s[i].swapNode(s[j]);
	}
	if (comp(s[j].cells[c].innerText, s[pivot].cells[c].innerText) != 0)
		s[j].swapNode(s[pivot]);
	quicksortDecrease(s, c, l, j - 1);
	quicksortDecrease(s, c, j + 1, r);
}

// var x = 0;
// sort as increase order
function doIncrease(alltr, columnIdx, from, to) {
	// x = 0;
	for (var j = from; j <= to; j++) {
		for (var k = from; k < to + from - j; k++) {
			// window.status=++x;
			var s = alltr[k].cells[columnIdx].innerText
			var t = alltr[k + 1].cells[columnIdx].innerText;
			if (!isNaN(s) && !isNaN(t)) {
				// compare as number
				var n = Number(s);
				var m = Number(t);
				if (n > m)
					alltr[k].swapNode(alltr[k + 1]);
			} else if (s.isValidDate() && t.isValidDate()) {
				// compare as date
				if (compareDate(s, t) == 1) {
					alltr[k].swapNode(alltr[k + 1]);
				}
			} else if (s.isValidTime() && t.isValidTime()) {
				// compare as time
				if (compareTime(s, t) == 1) {
					alltr[k].swapNode(alltr[k + 1]);
				}
			} else if (s.isValidDateTime() && t.isValidDateTime()) {
				// compare as date time
				if (compareDateTime(s, t) == 1) {
					alltr[k].swapNode(alltr[k + 1]);
				}
			} else {
				// compare as string
				if (s > t)
					alltr[k].swapNode(alltr[k + 1]);
			}
		}
	}
}

// sort as decrease order
function doDecrease(alltr, columnIdx, from, to) {
	// x = 0;
	for (var j = from; j <= to; j++) {
		for (var k = from; k < to + from - j; k++) {
			// window.status=++x;
			var s = alltr[k].cells[columnIdx].innerText
			var t = alltr[k + 1].cells[columnIdx].innerText;
			if (!isNaN(s) && !isNaN(t)) {
				// compare as number
				var n = Number(s);
				var m = Number(t);
				if (n < m)
					alltr[k].swapNode(alltr[k + 1]);
			} else if (s.isValidDate() && t.isValidDate()) {
				// compare as date
				if (compareDate(s, t) == -11) {
					alltr[k].swapNode(alltr[k + 1]);
				}
			} else if (s.isValidTime() && t.isValidTime()) {
				// compare as time
				if (compareTime(s, t) == -1) {
					alltr[k].swapNode(alltr[k + 1]);
				}
			} else if (s.isValidDateTime() && t.isValidDateTime()) {
				// compare as date time
				if (compareDateTime(s, t) == -1) {
					alltr[k].swapNode(alltr[k + 1]);
				}
			} else {
				// compare as string
				if (s < t)
					alltr[k].swapNode(alltr[k + 1]);
			}
		}
	}
}

function compareDate(d1, d2) {
	var result1 = d1.match(/^(\d{1,4})(.|-|\/)(\d{1,2})\2(\d{1,2})$/);
	var d1 = new Date(result1[1], result1[3] - 1, result1[4]);
	var result2 = d2.match(/^(\d{1,4})(.|-|\/)(\d{1,2})\2(\d{1,2})$/);
	var d2 = new Date(result2[1], result2[3] - 1, result2[4]);

	if (d1 > d2)
		return 1;
	if (d1 < d2)
		return -1;
	return 0;
}

function compareTime(t1, t2) {
	var result1 = t1.match(/^(\d{1,2})(:)?(\d{1,2})\2(\d{1,2})$/);
	var t1 = new Date("2000", "01", "01", result1[1], result1[3], result1[4]);
	var result2 = t2.match(/^(\d{1,2})(:)?(\d{1,2})\2(\d{1,2})$/);
	var t2 = new Date("2000", "01", "01", result2[1], result2[3], result2[4]);

	if (t1 > t2)
		return 1;
	if (t1 < t2)
		return -1;
	return 0;
}

function compareDateTime(dt1, dt2) {
	var result1 = dt1
			.match(/^(\d{1,4})(.|-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
	var dt1 = new Date(result1[1], result1[3] - 1, result1[4], result1[5],
			result1[6], result1[7]);
	var result2 = dt2
			.match(/^(\d{1,4})(.|-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
	var dt2 = new Date(result2[1], result2[3] - 1, result2[4], result2[5],
			result2[6], result2[7]);

	if (dt1 > dt2)
		return 1;
	if (dt1 < dt2)
		return -1;
	return 0;
}

// String.prototype.isValidDate=function()
function isValidDate(t) {
	var result = t.match(/^(\d{1,4})(.|-|\/)(\d{1,2})\2(\d{1,2})$/);
	if (result == null)
		return false;
	var d = new Date(result[1], result[3] - 1, result[4]);
	return (d.getFullYear() == result[1] && d.getMonth() + 1 == result[3] && d
			.getDate() == result[4]);
}

// String.prototype.isValidTime=function()
function isValidTime(t) {
	var result = t.match(/^(\d{1,2})(:)?(\d{1,2})\2(\d{1,2})$/);
	if (result == null)
		return false;
	if (result[1] > 24 || result[3] > 60 || result[4] > 60)
		return false;
	return true;
}

// String.prototype.isValidDateTime=function()
function isValidDateTime(t) {
	var result = t
			.match(/^(\d{1,4})(.|-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
	if (result == null)
		return false;
	var d = new Date(result[1], result[3] - 1, result[4], result[5], result[6],
			result[7]);
	return (d.getFullYear() == result[1] && (d.getMonth() + 1) == result[3]
			&& d.getDate() == result[4] && d.getHours() == result[5]
			&& d.getMinutes() == result[6] && d.getSeconds() == result[7]);
}
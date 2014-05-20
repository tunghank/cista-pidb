function $import(path) {
	var base, src = "all.js", scripts = document.getElementsByTagName("script");
	for (var i = 0; i < scripts.length; i++) {
		if (scripts[i].src.match(src)) {
			base = scripts[i].src.replace(src, "");
			break;
		}
	}
	document.write("<" + "script src=\"" + base + path + "\"></script>");
}

$import("prototype.js");
$import("zpmenu/utils/zapatec.js");

var globalHandler = {
	onCreate : function() {
		if ($('inProcessing')) {
			Element.show('inProcessing');
		}
	},

	onComplete : function() {
		if (Ajax.activeRequestCount == 0) {
			if ($('inProcessing')) {
				Element.hide('inProcessing');
			}
		}
	}
}

Ajax.Responders.register(globalHandler);
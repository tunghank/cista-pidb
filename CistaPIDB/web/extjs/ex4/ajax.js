(function(){
	Ext.onReady(function(){
		Ext.regModel("person",{
			fields:[
				{name:'name',type:'string'}
			]
		});
		
		var ajaxProxy = new Ext.data.proxy.Ajax({
			url:'person.jsp',
			model:'person',
			reader:'json',
			limitParam : 'indexLimit'
		});
		
		ajaxProxy.doRequest(new Ext.data.Operation({
				action:'read',
				limit:10,//分頁
				start :1,//分頁
				sorters:[
					new Ext.util.Sorter({
						property:'name',
						direction:'ASC'
					})
				]
			}),function(o){

			var text = o.response.responseText;
			alert(Ext.JSON.decode(text)['name']);
		});
		
	});
})();
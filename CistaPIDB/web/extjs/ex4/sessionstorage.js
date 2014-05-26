(function(){
	Ext.onReady(function(){
		Ext.regModel("user",{
			fields:[
				{name:'name',type:'string'}
			],
			proxy:{
				type:'sessionstorage',
				id  : 'twitter-Searches'
			}
		});
		//利用store來初始化數據
		var store = new Ext.data.Store({
			model:user
		});
		store.add({name:'Hank5'});
		store.sync();
		store.load();
		var msg = [];
		store.each(function(rec){
			msg.push(rec.get('name'));
		});
		alert(msg.join("\n"));
	})
})();
	
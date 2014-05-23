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
		//我们用store来初始化数据
		var store = new Ext.data.Store({
			model:user
		});
		store.add({name:'yunfengcheng'});
		store.sync();
		store.load();
		var msg = [];
		store.each(function(rec){
			msg.push(rec.get('name'));
		});
		alert(msg.join("\n"));
	})
})();
	
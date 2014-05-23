(function(){
	Ext.onReady(function(){
		Ext.regModel("user",{
			fields:[
				{name:'name',type:'string'}
			],
			proxy:{
				type:'localstorage',
				id  : 'twitter-Searches'
			}
		});
		//������store����ʼ������
		var store = new Ext.data.Store({
			model:user
		});
		store.add({name:'uspcat.com'});
		store.sync();
		store.load();
		var msg = [];
		store.each(function(rec){
			msg.push(rec.get('name'));
		});
		alert(msg.join("\n"));
	})
})();
	
(function(){
	Ext.onReady(function(){
		Ext.regModel("user",{
			fields:[
				{name:'name',type:'string'},
				{anem:'age',type:'int'}
			]
		});
		//不用create方法 我们直接用proxy来创建对象数据
		var userData = [
			{name:'uspcat.com',age:1},
			{name:'yunfengcheng',age:26}
		];
		//创建model的代理类
		var memoryProxy = Ext.create("Ext.data.proxy.Memory",{
			data:userData,
			model:'user'
		})
		
		userData.push({name:'new uspcat.com',age:1});
		//update
		memoryProxy.update(new Ext.data.Operation({
			action:'update',
			data:userData
		}),function(result){},this);
		
		//就可以做CRUD了
		memoryProxy.read(new Ext.data.Operation(),function(result){
			var datas = result.resultSet.records;
			Ext.Array.each(datas,function(model){
				alert(model.get('name'));
			})
		});
	});
})();
(function(){
	Ext.onReady(function(){
		Ext.regModel("user",{
			fields:[
				{name:'name',type:'string'},
				{anem:'age',type:'int'}
			]
		});
		
		//不用Create方法直接用proxy來創建對象
		var userData = [
			{name:'Hank1',age:1},
			{name:'Hank2',age:26}
		];
		
		
		//創建model的代理類
		var memoryProxy = Ext.create("Ext.data.proxy.Memory",{
			data:userData,
			model:'user'
		})
		
//		userData.push({name:'new uspcat.com',age:1});
//		//update
//		memoryProxy.update(new Ext.data.Operation({
//			action:'update',
//			data:userData
//		}),function(result){},this);
		
		//做CRUD
		
		memoryProxy.read(new Ext.data.Operation(),function(result){
			//alert(result);
			var datas = result.resultSet.records;
			Ext.Array.each(datas,function(model){
				alert(model.get('name'));
			})
		});
		
		
	});
})();
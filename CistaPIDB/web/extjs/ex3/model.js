(function(){
	Ext.onReady(function(){
		//我们利用Ext.define来创建我们的模型类
		//DB table person(name,age,email)
		Ext.define("person",{
			extend:"Ext.data.Model",
			fields:[
				{name:'name',type:'auto'},
				{name:'age',type:'int'},
				{name:'email',type:'auto'}
			]
		});
		//MVC模式中model一定是M层
		Ext.regModel("user",{
			fields:[
				{name:'name',type:'auto'},
				{name:'age',type:'int'},
				{name:'email',type:'auto'}
			]
		});
		//实例化我们的person类
		//1.new关键字
		var p = new person({
			name:'uspcat.com',
			age:26,
			email:'yunfengcheng2008@126.com'
		});
		//alert(p.get('name'));
		var p1 = Ext.create("person",{
			name:'uspcat.com',
			age:26,
			email:'yunfengcheng2008@126.com'
		});
		//alert(p1.get('age'));
		var p2 = Ext.ModelMgr.create({
			name:'uspcat.com',
			age:26,
			email:'yunfengcheng2008@126.com'
		},'person');
		//alert(p2.get('email'));
		//alert(p2.getName());//? class object.getClass.getName 
		alert(person.getName());
	});
})();






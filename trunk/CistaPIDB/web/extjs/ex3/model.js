(function(){
	Ext.onReady(function(){

		//DB table person(name,age,email)
		Ext.define("person",{
			extend:"Ext.data.Model",
			fields:[
				{name:'name',type:'auto'},
				{name:'age',type:'int'},
				{name:'email',type:'auto'}
			]
		});
		//MVC中一行是M層
		Ext.regModel("user",{
			fields:[
				{name:'name',type:'auto'},
				{name:'age',type:'int'},
				{name:'email',type:'auto'}
			]
		});
		//實曆化
		//1.new 關鍵子
		var p = new person({
			name:'hank.tang',
			age:28,
			email:'tunghank@gamil.com'
		});
		alert(p.get('name'));
		
//		var p1 = Ext.create("person",{
//			name:'uspcat.com',
//			age:26,
//			email:'yunfengcheng2008@126.com'
//		});
//		//alert(p1.get('age'));
//		var p2 = Ext.ModelMgr.create({
//			name:'uspcat.com',
//			age:26,
//			email:'yunfengcheng2008@126.com'
//		},'person');
//		//alert(p2.get('email'));
//		//alert(p2.getName());//? class object.getClass.getName 
//		alert(person.getName());
		
	});
})();






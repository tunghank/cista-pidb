(function(){
	Ext.onReady(function(){
		//��������Ext.define���������ǵ�ģ����
		//DB table person(name,age,email)
		Ext.define("person",{
			extend:"Ext.data.Model",
			fields:[
				{name:'name',type:'auto'},
				{name:'age',type:'int'},
				{name:'email',type:'auto'}
			]
		});
		//MVCģʽ��modelһ����M��
		Ext.regModel("user",{
			fields:[
				{name:'name',type:'auto'},
				{name:'age',type:'int'},
				{name:'email',type:'auto'}
			]
		});
		//ʵ�������ǵ�person��
		//1.new�ؼ���
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






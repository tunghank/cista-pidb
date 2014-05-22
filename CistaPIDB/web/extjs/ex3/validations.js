(function(){
	Ext.data.validations.lengthMessage = "����ĳ���";
	Ext.onReady(function(){
		//��չҲ���������Զ�����֤���Ƶĵ�һ���µ���֤����
		Ext.apply(Ext.data.validations,{
			age:function(config, value){
				var min = config.min;
				var max = config.max;
				if(min <= value && value<=max){
					return true;
				}else{
					this.ageMessage = this.ageMessage+"���ķ�ΧӦ����["+min+"~"+max+"]";
					return false;
				}
			},
			ageMessage:'age���ݳ��ֵ��˴���'
		});
		
		
		Ext.define("person",{
			extend:"Ext.data.Model",
			fields:[
				{name:'name',type:'auto'},
				{name:'age',type:'int'},
				{name:'email',type:'auto'}
			],
			validations:[
				{type:"length",field:"name",min:2,max:6},
				{type:'age',field:"age",min:0,max:150}
			]
		});
		var p1 = Ext.create("person",{
			name:'uspcat.com',
			age:-26,
			email:'yunfengcheng2008@126.com'
		});	
		var errors = p1.validate();
		var errorInfo = [];
		errors.each(function(v){
			errorInfo.push(v.field+"  "+v.message);
		});
		alert(errorInfo.join("\n"));
	});
	/**
	 * name 2~6
	 * set(String name){
	 * 	 if(){
	 * 	 }else{
	 * 	 }
	 * }
	 */
	//age ����С��0Ҳ���ܴ���150
})();










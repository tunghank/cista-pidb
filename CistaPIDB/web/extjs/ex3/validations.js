(function(){
	//Ext.data.validations.lengthMessage = "Length too long or too short";
	Ext.onReady(function(){
		
		//改造 自定義驗證Function
		Ext.apply(Ext.data.validations,{
			age:function(config, value){
				var min = config.min;
				var max = config.max;
				if(min <= value && value<=max){
					return true;
				}else{
					this.ageMessage = this.ageMessage+" Length ["+min+"~"+max+"]";
					return false;
				}
			},
			ageMessage:'不能小於0 也不能大於150'
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
			//alert(v);
			errorInfo.push(v.field+"  "+v.message);
		});
		alert(errorInfo.join("\n"));
	});
	
	
	/**
	 * name 2~6 字元
	 * set(String name){
	 * 	 if(name 長度 > 6){
	 * 	 }else{
	 * 	 }
	 * }
	 */
	//age不能小於0 也不能大於150
})();










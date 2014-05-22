(function(){
	Ext.data.validations.lengthMessage = "错误的长度";
	Ext.onReady(function(){
		//扩展也就是我们自定义验证机制的的一个新的验证方法
		Ext.apply(Ext.data.validations,{
			age:function(config, value){
				var min = config.min;
				var max = config.max;
				if(min <= value && value<=max){
					return true;
				}else{
					this.ageMessage = this.ageMessage+"他的范围应该是["+min+"~"+max+"]";
					return false;
				}
			},
			ageMessage:'age数据出现的了错误'
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
	//age 不能小于0也不能大于150
})();










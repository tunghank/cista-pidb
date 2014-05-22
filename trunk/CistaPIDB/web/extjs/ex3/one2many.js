(function(){
	Ext.onReady(function(){
		//Class 老師
		Ext.regModel("teacher",{
			fideld:[
				{name:'teacherId',type:"int"},
				{name:'name',type:"auto"}
			],
			hasMany:{
				 model: 'student',
			     name : 'getStudent',
       			 filterProperty: 'teacherId' //關連
			}
		});
		
		//學生
		Ext.regModel("student",{
			fideld:[
				{name:'studentId',type:"int"},
				{name:'name',type:"auto"},
				{name:"teacherId",type:'int'}
			]
		});
		//t.students 得到類的一個store的數據集合
	})
})();
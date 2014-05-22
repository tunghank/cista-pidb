(function(){
	Ext.onReady(function(){
		//类老师
		Ext.regModel("teacher",{
			fideld:[
				{name:'teacherId',type:"int"},
				{name:'name',type:"auto"}
			],
			hasMany:{
				 model: 'student',
			     name : 'getStudent',
       			 filterProperty: 'teacher_Id'
			}
		});
		//学生
		Ext.regModel("student",{
			fideld:[
				{name:'studentId',type:"int"},
				{name:'name',type:"auto"},
				{name:"teacher_Id",type:'int'}
			]
		});
		//t.students 得到子类的一个store数据集合
	})
})();
(function(){
	Ext.onReady(function(){
		Ext.regModel("person",{
			fields:[
				{name:'name',type:'string'}
			],
			proxy:{
				type:'jsonp',
				url:'http://www.uspcat.com/extjs/person.php'
			}
		});
		
		var person = Ext.ModelManager.getModel('person');
		person.load(1,{
			scope:this,
			success:function(model){
				alert(model.get('name'));
			}
		});
		
	})
})();
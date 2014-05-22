(function(){
	Ext.onReady(function(){
		Ext.define("person",{
			extend:"Ext.data.Model",
			fields:[
				{name:'name',type:'auto'},
				{name:'age',type:'int'},
				{name:'email',type:'auto'}
			],
			proxy:{
				type:'ajax',
				url:'person.jsp'
			}
		});
		var p = Ext.ModelManager.getModel("person");
		p.load(1, {
	        scope: this,
	        failure: function(record, operation) {
	        },
	        success: function(record, operation) {
	        	alert(record.data.name)
	        },
	        callback: function(record, operation) {
	        }
    	});
	})
})();
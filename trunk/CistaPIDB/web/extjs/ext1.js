

(function() {
	Ext.onReady(function(){
		//alert(0);
		//Ext.MessageBox.alert("Hello", "Hello World");
		var myArray = [1,2,3,4,-3,-4];
		Ext.Array.every(myArray, function(item){
			if(item>0){
				return true;
			}else{
				//<=0
				//alert(item);
				return false;
			}
		
		},this);
		
		var newArray = Ext.Array.filter(myArray,function(item){
			if(item>0){
				return true;
			}else{
				return false;
			}
		
		},this);
		//alert(newArray.join("\n"));
		Object.prototype.get = function(key, defaultValue){
			if(this[key]){
				return this[key];
			}else{
				if(defaultValue){
					return defaultValue;
				}
			}
		}
		var person = {
			name:"Hank",
			age:28
		}
		//alert(person['name']);
		alert(person.get('name'));
		
	});

})();

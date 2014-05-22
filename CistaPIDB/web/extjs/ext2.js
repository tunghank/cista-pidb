//function user(){
//	//Java �onstructor , Public
//	this.name = "Hank";
//	this.age = 26;
//	//Private
//	var email = "hank@gmail"
//	this.getEmail = function(){
//		return email;
//	}
//}
//
//var u = new user();
////alert(u.name)
////alert(u.age)
////alert(u.getEmail());
//
//var person = {
//	name:"Hank",
//	age:28
//}
//alert(person.name + " " + person["age"]);



(function() {
	
	//動態引入
	Ext.Loader.setConfig({
		enabled:true,
		paths:{
			myApp:'extjs/ux'
		}
	});
	
	
	Ext.onReady(function(){
		/**
		var win = new Ext.window.Window({
			title: 'Hello',
		    height: 200,
		    width: 400,
		    layout: 'fit'
		});
//		win.show();
		
		//1.Get Button Dom
		//2.Event
		//3.Show Windows
		
		Ext.get("myButton").on("click",function(){
			//alert("Show");
			win.show();
		});
		*/
		
//		var o = {
//			say: function(){
//				alert('Hank')
//			}
//		}
//		
//		var aliasFn = Ext.Function.alias(o,'say');
//		alert(aliasFn);
//		alert(aliasFn());
		
//		var win = Ext.create('Ext.window.Window',{
//			title: 'Hello',
//		    height: 200,
//		    width: 400,
//		    layout: 'fit'
//		});
//		win.show();


//		//動態引入
//		Ext.create("ux.myWin",{
//			title: 'Hello2',
//			requires:['ux.myWin'] //動態引入
//		}).show();
		
		
		Ext.get("myButton").on("click",function(){
			//動態引入
			Ext.create("ux.myWin",{
				title: 'Hello2',
				requires:['ux.myWin'] //動態引入
			}).show();
			
			var win1 = Ext.create("ux.myWin",{
				title: 'Hello2',
				price: 600,
				requires:['ux.myWin'] //動態引入
			});
			
			alert(win1.getPrice());
		});		
		
		//mixins
		Ext.define('say', {
		     canSay: function() {
		         alert("Hello");
			 }
		});
		
		Ext.define('sing', {
		     sing: function() {
		         alert("Hello 123")
		     }
		});
		
		
		Ext.define('user', {
			//extend:'say'
		     mixins: {
		     	say:'say',
		     	sing:'sing'
		     }
		});
		
		var u = Ext.create("user",{});
		u.canSay();
		u.sing();
		
	});

})();

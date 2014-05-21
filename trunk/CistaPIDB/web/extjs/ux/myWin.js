		Ext.define("ux.myWin",{
			extend:"Ext.window.Window",
			newtitle:"Hank 123",
			mySetTitle:function(){
				this.title = this.newtitle;
			},
			title: 'Hello',
		    height: 200,
		    width: 400,
		    layout: 'fit',
		    config: {
		         price: 500
		     },
			initComponent: function() {
				this.mySetTitle();
				this.callParent(arguments);
			}
			
		})
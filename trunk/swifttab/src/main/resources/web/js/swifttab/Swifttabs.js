/* Mtabs.js

	Purpose:

	Description:

	History:
		2010/9/20, Created by TonyQ

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/
//Because i use the class type to tell swifttab that we are movable , so I save the class here.
swifttab.Swifttabs = zk.$extends(zul.tab.Tabs, {
    $define:{
      movable:function(movable){
         for (var w = this.firstChild,ind=0; w; w = w.nextSibling,ind++) {
            if(w.setMovable_){
                w.setMovable_(movable);
            }
        }  
      }
    }
});


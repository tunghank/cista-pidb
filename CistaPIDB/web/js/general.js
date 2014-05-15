 //判断是否是数字,sign不用传
 function IsNumber(string,sign)
 {
   var number;
   if ((sign!=null) && (sign!="&acute;-&acute;") && (sign!="&acute;+&acute;"))
   {
     return false;
   }
   number = new Number(string);
   if (isNaN(number))
   {
     return false;
   }
   else if ((sign==null) || (sign=="&acute;-&acute;" && number<0) || (sign=="&acute;+&acute;" && number>0))
   {
     return true;
   }
   else
   return false;
 }


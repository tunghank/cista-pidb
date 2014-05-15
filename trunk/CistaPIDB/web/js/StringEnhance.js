// Function Name: trim
// Function Description: \uFFFD戊\uFFFD趼睫揹腔忑帣腔諾跡
// Creation Date: 2004-7-13 15:30
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.trim=function(){
return this.replace(/(^\s*)|(\s*$)/g, "");
}

// Function Name: ltrim
// Function Description: \uFFFD戊\uFFFD趼睫揹腔酘耜腔諾跡
// Creation Date: 2004-7-13 9:58
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.ltrim=function()
{
return this.replace(/(^\s*)/g, "");
}

// Function Name: rtrim
// Function Description: \uFFFD戊\uFFFD趼睫揹腔衵耜腔諾跡
// Creation Date: 2004-7-13 15:31
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.rtrim=function()
{
return this.replace(/(\s*$)/g, "");
}

// Function Name: len
// Function Description: 殿隙趼睫揹腔妗暱酗僅, 珨跺犖趼呾2跺酗僅
// Creation Date: 2004-7-13 9:58
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.len=function()
{
var str=this;
return str.replace(/[^\x00-\xff]/g, "**").length
}

// Function Name: isValidDate
// Function Description: 瓚剿怀\uFFFD輮鯓俴孝譯昈梪皒鯓\uFFFD - "YYYY-MM-DD"
// Creation Date: 2004-7-13 9:58
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidDate=function()
{
var result=this.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
if(result==null) return false;
var d=new Date(result[1], result[3]-1, result[4]);
return (d.getFullYear()==result[1]&&d.getMonth()+1==result[3]&&d.getDate()==result[4]);
}

// Function Name: isValidTime
// Function Description: 瓚剿怀\uFFFD輮鯓俴孝騫掉銝鯓\uFFFD - "HH:MM:SS"
// Creation Date: 2004-7-13 9:58
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidTime=function()
{
var result=this.match(/^(\d{1,2})(:)?(\d{1,2})\2(\d{1,2})$/);
if (result==null) return false;
if (result[1]>24 || result[3]>60 || result[4]>60) return false;
return true;
}

// Function Name: isValidEmail
// Function Description: 瓚剿怀\uFFFD輮鯓俴孝警諓衶宎\uFFFD
// Creation Date: 2004-7-13 9:59
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidEmail=function()
{
var result=this.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidDatetime
// Function Description: 瓚剿怀\uFFFD輮鯓俴孝議午梪皒鯓\uFFFD - "YYYY-MM-DD HH:MM:SS"
// Creation Date: 2004-7-13 9:59
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidDatetime=function()
{
var result=this.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
if(result==null) return false;
var d= new Date(result[1], result[3]-1, result[4], result[5], result[6], result[7]);
return (d.getFullYear()==result[1]&&(d.getMonth()+1)==result[3]&&d.getDate()==result[4]&&d.getHours()==result[5]&&d.getMinutes()==result[6]&&d.getSeconds()==result[7]);
}

//check for 2004/11/12 08:30
String.prototype.isValidDatetime2=function()
{
var result=this.match(/^(\d{1,4})(\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2})$/);
if(result==null) return false;
var d= new Date(result[1], result[3]-1, result[4], result[5], result[6], 0);
return (d.getFullYear()==result[1]&&(d.getMonth()+1)==result[3]&&d.getDate()==result[4]&&d.getHours()==result[5]&&d.getMinutes()==result[6]);
}

//check for 2004/11/12
String.prototype.isValidDatetime3=function()
{
var result=this.match(/^(\d{1,4})(\/)(\d{1,2})\2(\d{1,2})$/);
if(result==null) return false;
var d= new Date(result[1], result[3]-1, result[4], 0, 0, 0);
return (d.getFullYear()==result[1]&&(d.getMonth()+1)==result[3]&&d.getDate()==result[4]);
}

// Function Name: isValidInteger
// Function Description: 瓚剿怀\uFFFD輮鯓遘龒\uFFFD杅
// Creation Date: 2004-7-13 10:01
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidInteger=function()
{
var result=this.match(/^(-|\+)?\d+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidPositiveInteger
// Function Description: 瓚剿怀\uFFFD輮鯓遘龒\uFFFD淕杅
// Creation Date: 2004-7-13 10:01
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidPositiveInteger=function()
{
var result=this.match(/^\d+$/);
if(result==null) return false;
if(Number(this)==0) return false;
return true;
}

// Function Name: isValidNegativeInteger
// Function Description: 瓚剿怀\uFFFD輮鯓遘鷏禎\uFFFD杅
// Creation Date: 2004-7-13 10:28
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidNegativeInteger=function()
{
var result=this.match(/^-\d+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidNumber
// Function Description: 瓚剿怀\uFFFD輮鯓遘麜\uFFFD趼
// Creation Date: 2004-7-13 10:01
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidNumber=function()
{
return !isNaN(this);
}

// Function Name: isValidLetters
// Function Description: 瓚剿怀\uFFFD輮鯓遘齥\uFFFD A-Z / a-z 郪傖腔趼睫揹
// Creation Date: 2004-7-13 10:10
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidLetters=function()
{
var result=this.match(/^[a-zA-Z]+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidDigits
// Function Description: 瓚剿怀\uFFFD輮鯓遘齥\uFFFD 0-9 郪傖腔杅趼
// Creation Date: 2004-7-13 10:10
// Last Modify By: href
// Last Modify Date: 2004-08-09 11:39
// Modify history: /[0-9]+$/
//
String.prototype.isValidDigits=function()
{
var result=this.match(/^[0-9]+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidAlphanumeric
// Function Description: 瓚剿怀\uFFFD輮鯓遘齥\uFFFD 0-9 / A-Z / a-z 郪傖腔趼睫揹
// Creation Date: 2004-7-13 10:14
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidAlphanumeric=function()
{
var result=this.match(/^[a-zA-Z0-9]+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidString
// Function Description: 瓚剿怀\uFFFD輮鯓遘齥\uFFFD 0-9 / A-Z / a-z / . / _ 郪傖腔趼睫揹
// Creation Date: 2004-7-13 10:20
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidString=function()
{
var result=this.match(/^[a-zA-Z0-9\s.\-_]+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidPostalcode
// Function Description: 瓚剿怀\uFFFD輮鯓遘齥俴孝鼯彶\uFFFD晤鎢
// Creation Date: 2004-7-13 10:22
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidPostalcode=function()
{
var result=this.match(/(^[0-9]{6}$)/);
if(result==null) return false;
return true;
}

// Function Name: isValidPhoneNo
// Function Description: 瓚剿怀\uFFFD輮鯓遘齥俴孝警蝏偕鷓\uFFFD
// Creation Date: 2004-7-13 10:22
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidPhoneNo=function()
{
var result=this.match(/(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)/);
if(result==null) return false;
return true;
}

// Function Name: isValidMobileNo
// Function Description: 瓚剿怀\uFFFD輮鯓遘齥俴孝騫硐\uFFFD瘍鎢
// Creation Date: 2004-7-13 10:23
// Last Modify By: N/A
// Last Modify Date: N/A
String.prototype.isValidMobileNo=function()
{
var result=this.match(/(^0{0,1}13[0-9]{9}$)/);
if(result==null) return false;
return true;
}

//fill special char 'c' to the right side of string, until length of string is 'toLenth'.
//Usage: var = "test"; //"test000000"
//       a = a.fillCharRight('0', 10);
//Author: href
String.prototype.fillCharRight=function(c, toLenth) {
	var str = this;
	while(str.len()<toLenth)
		str += c;
	return str;
}

//fill special char 'c' to the left side of string, until length of string is 'toLenth'.
//Usage: var a = "test"; //"000000test"
//       a = a.fillCharLeft('0', 10);
//Author: href
String.prototype.fillCharLeft=function(c, toLenth) {
	var str = this;
	while(str.len()<toLenth)
		str = c + str;
	return str;
}

//check a decimal
//Usage: var a = "12344.5678";
//       a.isDecimal(6, 3); //return false
//Author: href
String.prototype.isDecimal=function(intLen, decLen) {
	var decimal = this;
	// allow end with %
	if(decimal.substr(decimal.length-1)== "%" ){
		decimal = decimal.substr(0,decimal.lastIndexOf("%"));
	}
	if(this.charAt(0)=='+' || this.charAt(0)=='-')
		decimal = this.substring(1);
	var radix = decimal.indexOf('.');
	//拸苤杅萸ㄛ硐衄淕杅窒煦
	if(radix==-1) {
		if(decimal.len()>intLen)
			return false;

		if((decimal.isValidPositiveInteger() || decimal==0) && decimal.len()<=intLen)
			return true;
		else {
			return false;
		}
	}

	var intPart = decimal.substring(0, radix);
	var decPart = decimal.substring(radix+1);

	if(!(intPart.isValidPositiveInteger() || intPart=='0'))
		return false;
	if(intPart.len()>intLen)
		return false;

	if(!(decPart.len()<=decLen && decPart.isValidDigits()))
		return false;

	return true;
}

//check a positive decimal
//Usage: var a = "-12344.58";
//       a.isPositiveDecimal(6, 3); //return false, it is negative
//Author: href
String.prototype.isPositiveDecimal=function(intLen, decLen) {
	if(this.charAt(0)!='-' && this.isDecimal(intLen, decLen))
		return true;
	return false;
}

//check string is a valid 3 digits year or less than 3 digits
//Usage: var a = "123";
//       a.is3digitsYear(); //return true;
//Author: href
String.prototype.is3digitsYear=function() {
	if(this.length<=3 && this.isValidDigits() && Number(this) >= 30)
		return true;

	return false;
}

String.prototype.splitBy=function(delim) {
	var result = new Array();
	var temp = "";
	for(var i=0; i<this.len(); i++) {
		var c = this.charAt(i);
		if(delim.indexOf(c)<0) {
			temp += c;
		} else {
			if(temp.length!=0) {
				result.push(temp);
			}
			temp = "";
		}
	}
	if(temp.length!=0) {
		result.push(temp);	
	}
	return result;
}


String.prototype.isNumber = function() {
  var s = this.Trim();
// allow end with %
  if(s.substr(s.length-1)== "%" ){
  	s = s.substr(0,s.lastIndexOf("%"));
  }
  return (s.search(/^[+-]?[0-9.]*$/) >= 0);
}

String.prototype.isDate = function() {
	    //Set Date Format
        var reg = /^(\d{4})\/(\d{1,2})\/(\d{1,2})$/;
        var r = this.match(reg);
        if(r==null) return (false);
        var tDate = new Date(r[1],r[2]-1,r[3]);
        return (tDate.getFullYear()==r[1]&&(tDate.getMonth()+1)==r[2]&&tDate.getDate()==r[3]);
}

String.prototype.replaceAll  = function(s1,s2){   
	return this.replace(new RegExp(s1,"gm"),s2);   
}

String.prototype.Trim = function(){
        return this.replace(/(^\s*)|(\s*$)/g, "");
}
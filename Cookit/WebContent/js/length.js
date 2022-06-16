
document.getElementById("length-target").addEventListener("keyup", function(){ 

	var len = this.value.length;
	var len_field = document.getElementById("length-display");
	
	len_field.innerHTML = len;

 });
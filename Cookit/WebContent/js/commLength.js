
document.getElementById("comment").addEventListener("keyup", function(){ 

	var len = document.getElementById('comment').value.length;
	var len_field = document.getElementById("comm-length");
	
	len_field.innerHTML = len+"/250";
	
	if(len > 500){
		len_field.style.color = "red";
	} else {
		len_field.style.color = "black";
	}

 });
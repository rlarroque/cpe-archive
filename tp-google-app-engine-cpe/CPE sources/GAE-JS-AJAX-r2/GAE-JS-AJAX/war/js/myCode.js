$(document).ready(function(){
  $("#btnCommitId").click(function(){
	  	$.post("register",
    		  {
    		    cmd:"Register",
    		    email:$("#inputEmailId").val(),
    		    pwd:$("#inputPwdId").val(),
    		  },
    		  function(data,status){
    		    alert("Post Done new user added, id: " + data.userid + "\nStatus: " + status);
    		  });
  		});
});
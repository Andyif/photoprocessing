function validateDevForm() {
     var x = document.forms["devForm"]["firstName"].value;
     if (x == null || x == "") {
         alert("First name must be filled out");
         return false;
     }
 }

 function validateSkillForm() {
     var x = document.forms["skillForm"]["label"].value;
     if (x == null || x == "") {
         alert("Label must be filled out");
         return false;
     }
 }
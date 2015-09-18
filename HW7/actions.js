	function onPageLoad(){
				paramQuery = "<?php echo empty($_POST["keyword"])?"": $_POST["keyword"]; ?>";
				paramMinPrice = "<?php echo empty($_POST["minPrice"])?"": $_POST["minPrice"]; ?>";
				paramMaxPrice = "<?php echo empty($_POST["maxPrice"])?"": $_POST["maxPrice"]; ?>";
				paramCondition = "<?php echo empty($_POST["condition"])?"": implode(",", $_POST["condition"]); ?>";
				paramBuyingFormats = "<?php echo empty($_POST["buyingFormats"])?"": implode(",", $_POST["buyingFormats"]); ?>";
				paramSeller = "<?php echo empty($_POST["seller"])?"": $_POST["seller"]; ?>";
				paramFreeShipping = "<?php echo empty($_POST["freeShipping"])?"": $_POST["freeShipping"]; ?>";
				paramExpeditedShipping = "<?php echo empty($_POST["expeditedShipping"])?"": $_POST["expeditedShipping"]; ?>";
				paramMaxHandlingTime = "<?php echo empty($_POST["maxHandlingTime"])?"": $_POST["maxHandlingTime"]; ?>";
				paramSortOrder = "<?php echo empty($_POST["sortOrder"])?"": $_POST["sortOrder"]; ?>";
				paramResults = "<?php echo empty($_POST["results"])?"": $_POST["results"]; ?>";
				
				if(paramQuery!=""){ document.ebayForm.keyword.value = paramQuery; }
				if(paramMinPrice!=""){ document.ebayForm.minPrice.value = paramMinPrice;  }
				if(paramMaxPrice!=""){ document.ebayForm.maxPrice.value = paramMaxPrice;  }
	
				params = paramCondition.split(",");
				conditions = document.ebayForm["condition[]"];
				checkboxSet(params,conditions);

				params = paramBuyingFormats.split(",");
				conditions = document.ebayForm["buyingFormats[]"];
				checkboxSet(params,conditions);
				
				params = paramSeller.split(",");
				conditions = document.ebayForm["seller"];
				if(params[0] == document.ebayForm["seller"].value){
					conditions.checked = true;
				}

				params = paramFreeShipping.split(",");
				conditions = document.ebayForm["freeShipping"];
				if(params[0] == document.ebayForm["freeShipping"].value){
					conditions.checked = true;
				}

				params = paramExpeditedShipping.split(",");
				conditions = document.ebayForm["expeditedShipping"];
				if(params[0] == document.ebayForm["expeditedShipping"].value){
					conditions.checked = true;
				}
				if(paramMaxHandlingTime!=""){ document.ebayForm.maxHandlingTime.value = paramMaxHandlingTime;  }
				if(paramSortOrder!=""){ document.ebayForm.sortOrder.value = paramSortOrder; }
				if(paramResults!=""){ document.ebayForm.results.value = paramResults; }
		}
		function checkboxSet(params,conditions){
				console.log(params+" , "+conditions);
				for(i=0;i<params.length;i++){
					for(j=0;j<conditions.length;j++){
						console.log(params[i]+" -> "+conditions.item(j).value);
						if(conditions.item(j).value == params[i]){
							conditions.item(j).checked = true;
						}
					}
				}
		}
		function validation(){
			if(document.ebayForm.keyword.value==""){
				alert("Please enter value for Key Words");
				return false;
			}
			if(!isValidInt(document.ebayForm.minPrice.value)){
				if(document.ebayForm.minPrice.value!=""){
					alert("Invalid Min Price Value");
					return false;
				}
			}
			if(!isValidInt(document.ebayForm.maxPrice.value)){
				if(document.ebayForm.maxPrice.value!=""){
					alert("Invalid Max Price Value");
					return false;
				}
			}
			if(document.ebayForm.maxPrice.value!="" && document.ebayForm.maxPrice.value!="" && document.ebayForm.minPrice.value > document.ebayForm.maxPrice.value){
				alert("Minimum price cannot be greater than Maximum price");
				return false;
			}
			var maxHandlingTime = document.ebayForm.maxHandlingTime.value;
			if(maxHandlingTime!=""){
					if(!isValidInt(maxHandlingTime)){
					alert("Invalid Max Handling Time Value");
					return false;
				}
				else{
					if(maxHandlingTime<1){
						alert("Max handling time should be an integer greater than or equal to 1");
						return false;
					}
				}
			}
			return true;
		}
		function isValidInt(num){
			if (isNaN(num) || parseInt(Number(num)) != num || isNaN(parseInt(num, 10))){			
				return false;
			}
			return true;
		}
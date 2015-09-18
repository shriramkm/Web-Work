<html>
	<head>
	<style type="text/css">
		html, body {
			height: 100%;
		}

		html {
			display: table;
			margin: auto;
		}

		.main1 {
			border: 3px solid;
			margin: 0 340px 0 340px;
			border: 3px solid;
			padding: 10px;
		}

		.main2 {
			border: 3px solid;
			padding: 10px;
		}

		.form {
			border: 3px solid;
			padding: 5px;
			width: 600px;
		}

		.form td{
			padding-bottom: 4px;
			border-bottom: 1px solid lightgrey;
		}

		#logo img,span{
			vertical-align: middle;
			font-size: 20px
		}

		form input[type=submit] {
			width: 7em;
			float: right;
			padding-left: 5px;
		}

		form input[type=reset] {
			width: 7em;
			margin-left: 230px;
		}
	</style>
	<script type="text/javascript">
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
	</script>
	</head>
	<body onload="onPageLoad()">
		<div class="main1" align="center">
		<div id="logo" align="center"><img height="75" width="75" src="http://cs-server.usc.edu:45678/hw/hw6/ebay.jpg"/><span><b>Shopping</b><br/><br/></span></div>
		<div class="form" align="center"><form name="ebayForm" method="post" onsubmit="return validation()" action="processForm.php">
			<table>
				<tr><td><b>Key Words*:</b></td><td><input type="text" size="65" name="keyword"/></td></tr>
				<tr><td><b>Price Range:</b></td><td>from $ <input type="text" size="5" name="minPrice"/> to $ <input type="text" size="5" name="maxPrice"/></td></tr>
				<tr><td><b>Condition:</b></td><td><input type="checkbox" name="condition[]" value="1000"/>New
						  &emsp;<input type="checkbox" name="condition[]" value="3000"/>Used
						  &emsp;<input type="checkbox" name="condition[]" value="4000"/>Very Good
						  &emsp;<input type="checkbox" name="condition[]" value="5000"/>Good
						  &emsp;<input type="checkbox" name="condition[]" value="6000"/>Acceptable</td></tr>
				<tr><td><b>Buying formats:</b></td><td><input type="checkbox" name="buyingFormats[]" value="FixedPrice"/>Buy It Now
										  &emsp;<input type="checkbox" name="buyingFormats[]" value="Auction"/>Auction
										  &emsp;<input type="checkbox" name="buyingFormats[]" value="Classified"/>Classified Ads</td></tr>
				<tr><td><b>Seller:</b></td><td><input type="checkbox" name="seller" value="accepted"/>Return accepted</td></tr>
				<tr><td style="vertical-align: top" rowspan="3"><b>Shipping:</b></td><td style="border-bottom:0">  <input type="checkbox" name="freeShipping" value="free"/>Free Shipping</td></tr>
				<tr>								  <td style="border-bottom:0">	<input type="checkbox" name="expeditedShipping" value="expedited"/>Expedited shipping available</td></tr>
				<tr>								  <td>  Max handling time (days):<input type="text" size="5" name="maxHandlingTime"/></td></tr>
				<tr><td><b>Sort by:</b></td><td><select name="sortOrder">
											<option value="BestMatch">Best Match</option>
											<option value="CurrentPriceHighest">Price: Highest first</option>
											<option value="PricePlusShippingHighest">Price+Shipping: Highest first</option>
											<option value="PricePlusShippingLowest">Price+Shipping: Lowest first</option>
										</select></td></tr>
				<tr><td style="border-bottom:0"><b>Results Per Page:</b></td><td style="border-bottom:0"><select name="results">
											<option value="5">5</option>
											<option value="10">10</option>
											<option value="15">15</option>
											<option value="20">20</option>
										</select></td></tr>
				<tr><td style="border-bottom:0">&nbsp;</td><td style="border-bottom:0"><input type="reset" value="clear" width="10"/><input type="submit" value="search" width="10"/></td></tr>
			</table>
		</form></div>
		
		<?php
			// Generates an indexed URL snippet from the array of item filters
			function buildURLArray ($filterarray) {
			  global $urlfilter;
			  global $i;
			  // Iterate through each filter in the array
			  foreach($filterarray as $itemfilter) {
				// Iterate through each key in the filter
				if(!empty($itemfilter['value'])){
					foreach ($itemfilter as $key =>$value) {
					  if(is_array($value)) {
						foreach($value as $j => $content) { // Index the key for each value
						  $urlfilter .= "&itemFilter($i).$key($j)=$content";
						}
					  }
					  else {
						if($value != "") {
						  $urlfilter .= "&itemFilter($i).$key=$value";
						}
					  }
					}
					$i++;
				}
			  }
			  return "$urlfilter";
			} // End of buildURLArray function
		?>
		<?php
			error_reporting(E_ALL);  // Turn on all errors, warnings and notices for easier debugging
			if(!empty($_POST["keyword"])){
				// API request variables
				$endpoint = 'http://svcs.ebay.com/services/search/FindingService/v1';  // URL to call
				$siteid = '0';
				$appid = 'USCc2e945-9053-4833-9fe2-522b40e2634';  // Replace with your own AppID
				$version = '1.0.0';  // API version supported by your application
				$query = $_POST["keyword"];  // You may want to supply your own query
				$safequery = urlencode($query);  // Make the query URL-friendly
				$maxEntries = $_POST["results"];
				$sortOrder = $_POST["sortOrder"];
				$returnAccepted = (empty($_POST["seller"])) ? 'false' : (($_POST['seller']=="accepted") ? 'true' : 'false');
				$freeShipping = (empty($_POST["freeShipping"])) ? 'false' : (($_POST['freeShipping']=="free") ? 'true' : 'false');
				$expeditedShipping = (empty($_POST["expeditedShipping"])) ? '' : (($_POST['expeditedShipping']=="expedited") ? 'Expedited' : '');
				$maxHandlingTime = (empty($_POST["maxHandlingTime"])) ? '' : $_POST["maxHandlingTime"];

				$i = 0;

				// Create a PHP array of the item filters you want to use in your request
				$filterarray =
				  array(
					array(
					'name' => 'MinPrice',
					'value' => (empty($_POST["minPrice"])) ? '0.0' : $_POST["minPrice"],
					'paramName' => 'Currency',
					'paramValue' => 'USD'),
					array(
					'name' => 'MaxPrice',
					'value' => (empty($_POST["maxPrice"])) ? '999999999999999999999999999999999999999999999999999999999999.0' : $_POST["maxPrice"],
					'paramName' => 'Currency',
					'paramValue' => 'USD'),
					array(
					'name' => 'Condition.',
					'value' => (empty($_POST["condition"])) ? '' : $_POST["condition"],
					'paramName' => '',
					'paramValue' => ''),
					array(
					'name' => 'ListingType',
					'value' => (empty($_POST["buyingFormats"])) ? '' : $_POST["buyingFormats"],
					'paramName' => '',
					'paramValue' => ''),
					array(
					'name' => 'ReturnsAcceptedOnly',
					'value' => $returnAccepted,
					'paramName' => '',
					'paramValue' => ''),
					array(
					'name' => 'FreeShippingOnly',
					'value' => $freeShipping,
					'paramName' => '',
					'paramValue' => ''),
					array(
					'name' => 'ExpeditedShippingType',
					'value' => $expeditedShipping,
					'paramName' => '',
					'paramValue' => ''),
					array(
					'name' => 'MaxHandlingTime',
					'value' => $maxHandlingTime,
					'paramName' => '',
					'paramValue' => '')
				  );

				buildURLArray($filterarray);

				// Construct the findItemsAdvanced HTTP GET call
				$apicall = "$endpoint?";
				$apicall .= "siteid=$siteid";
				$apicall .= "&SECURITY-APPNAME=$appid";
				$apicall .= "&OPERATION-NAME=findItemsAdvanced";
				$apicall .= "&SERVICE-VERSION=$version";
				$apicall .= "&RESPONSE-DATA-FORMAT=XML&REST-PAYLOAD";
				$apicall .= "&keywords=$safequery";
				$apicall .= "&paginationInput.entriesPerPage=$maxEntries";
				$apicall .= "&sortOrder=$sortOrder";
				$apicall .= "$urlfilter";

				log.error($apicall);
				// Load the call and capture the document returned by eBay API
				$resp = simplexml_load_file( $apicall);

				// Check to see if the request was successful, else print an error
				if ($resp->ack == "Success") {
				  $results = '';
				  // If the response was loaded, parse it and build links
				  if($resp->paginationOutput->totalEntries == "0"){
					$results .= "<h3 align='center'>No results found</h3>";
				  }
				  else{
				  $results .= "</div><br/><div class='main2'><div align='center'><h3 align='center'>".$resp->paginationOutput->totalEntries." Results for ".$query."</h3>";
				  $results .= "<table align='center' style='border:1px solid'>";
				  foreach($resp->searchResult->item as $item) {
					$pic   = $item->galleryURL;
					$link  = $item->viewItemURL;
					$title = $item->title;
					$condition = $item->condition->conditionDisplayName;
					$topRated = $item->topRatedListing;
					$listingType = $item->listingInfo->listingType;
					$acceptReturn = $item->returnsAccepted;
					$expeditedShipping = $item->shippingInfo->expeditedShipping;
					$shippingTime = $item->shippingInfo->handlingTime;
					$price = $item->sellingStatus->convertedCurrentPrice;
					$shippingPrice = $item->shippingInfo->shippingServiceCost;
					$location = $item->location;

					// For each SearchResultItem node, build a link and append it to $results
					$results .= "<tr><td height='100%' width='25%' rowspan=\"10\"><img height='100%' width='100%' src=\"$pic\"></td><td style='padding:0 25px 0 25px'><a href=\"$link\">$title</a></td></tr>
								 <tr><td style='padding:0 25px 0 25px'><b>Condition : </b>$condition";
					$results .= ($topRated=='true')?"<img height=\"50\" width=\"50\" src=\"http://cs-server.usc.edu:45678/hw/hw6/itemTopRated.jpg\"/></td></tr>":"</td></tr>";
					
					$results .= "<tr><td>&nbsp;</td>";
		
					if ($listingType=='FixedPrice' || $listingType=='StoreInventory'){
						$results .= "<tr style='border:1px solid'><td style='padding:0 25px 0 25px'><b>Buy It Now</b></td>";
					}
					else if ($listingType== 'Auction'){
						$results .= "<tr style='border:1px solid'><td style='padding:0 25px 0 25px'><b>Auction</b></td>";
					}
					else if ($listingType== 'Classified'){
						$results .= "<tr style='border:1px solid'><td style='padding:0 25px 0 25px'><b>Auction</b></td>";
					}

					$results .= "<tr><td>&nbsp;</td>";

					$results .= ($acceptReturn=='true')?"<tr style='border:1px solid'><td style='padding:0 25px 0 25px'>Seller accepts return</td></tr>":"<tr><td style='padding:0 25px 0 25px'>Seller doesn't accept return</td></tr>";

					if ($shippingPrice=='0.0'){
						$results .= "<tr style='border:1px solid'><td style='padding:0 25px 0 25px'>FREE Shipping -- ";
					}
					else {
						$results .= "<tr style='border:1px solid'><td style='padding:0 25px 0 25px'>Shipping Not FREE -- ";
					}

					if ($expeditedShipping=='true'){
						$results .= "Expedited Shipping Available -- ";
					}
					else {
						$results .= "Expedited Shipping Not Available -- ";
					}

					$results .= "Handled for shipping in $shippingTime day(s)</td></tr>";
					$results .= "<tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td>";
					$results .= "<tr><td style='padding:0 25px 0 25px'><b>Price : \$$price ".(($shippingPrice==0.0)?"":"(+ \$$shippingPrice for shipping) ")."</b>
								<i>From $location</i></td></tr>";
					$results .= "<tr><td style='padding:0 !important' colspan='2'><hr></td></tr>";
				  }
				  $results .= "</table>";
				}
				}
				// If the response does not indicate 'Success,' print an error
				else {
				  $results  = "<h3>Oops! The request was not successful. Make sure you are using a valid ";
				  $results .= "AppID for the Production environment.</h3>";
				}
				$results .= "</div>";

				print_r( $results );
			}
		?>
		<script type="text/javascript">
			document.getElementsByClassName("main2")[0].style["margin-left"] = (screen.width - document.getElementsByClassName("main2")[0].children[0].children[1].offsetWidth)/2 - 25;
			document.getElementsByClassName("main2")[0].style["margin-right"] = (screen.width - document.getElementsByClassName("main2")[0].children[0].children[1].offsetWidth)/2 - 25;
		</script>
		</div>
	</body>
</html>
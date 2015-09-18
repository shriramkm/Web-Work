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

			error_reporting(E_ALL);  // Turn on all errors, warnings and notices for easier debugging
			if(!empty($_GET["keyword"])){
				// API request variables
				$endpoint = 'http://svcs.ebay.com/services/search/FindingService/v1';  // URL to call
				$siteid = '0';
				$appid = 'USCc2e945-9053-4833-9fe2-522b40e2634';  // Replace with your own AppID
				$version = '1.0.0';  // API version supported by your application
				$query = $_GET["keyword"];  // You may want to supply your own query
				$safequery = urlencode($query);  // Make the query URL-friendly
				$maxEntries = $_GET["results"];
				$sortOrder = $_GET["sortOrder"];
				$returnAccepted = (empty($_GET["seller"])) ? '' : (($_GET['seller']=="accepted") ? 'true' : '');
				$freeShipping = (empty($_GET["freeShipping"])) ? 'false' : (($_GET['freeShipping']=="free") ? 'true' : 'false');
				$expeditedShipping = (empty($_GET["expeditedShipping"])) ? '' : (($_GET['expeditedShipping']=="expedited") ? 'Expedited' : '');
				$maxHandlingTime = (empty($_GET["maxHandlingTime"])) ? '' : $_GET["maxHandlingTime"];
				$pageNum = (empty($_GET["pageNumber"])) ? '1' : $_GET["pageNumber"];

				$i = 0;

				// Create a PHP array of the item filters you want to use in your request
				$filterarray =
				  array(
					array(
					'name' => 'MinPrice',
					'value' => (empty($_GET["minPrice"])) ? '0.0' : $_GET["minPrice"],
					'paramName' => '',
					'paramValue' => ''),
					array(
					'name' => 'MaxPrice',
					'value' => (empty($_GET["maxPrice"])) ? '999999999999999999999999999999999999999999999999999999999999.0' : $_GET["maxPrice"],
					'paramName' => '',
					'paramValue' => ''),
					array(
					'name' => 'Condition',
					'value' => (empty($_GET["condition"])) ? '' : $_GET["condition"],
					'paramName' => '',
					'paramValue' => ''),
					array(
					'name' => 'ListingType',
					'value' => (empty($_GET["buyingFormats"])) ? '' : $_GET["buyingFormats"],
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
				$apicall .= "&RESPONSE-DATA-FORMAT=XML";
				$apicall .= "&keywords=$safequery";
				$apicall .= "&paginationInput.entriesPerPage=$maxEntries";
				$apicall .= "&sortOrder=$sortOrder";
				$apicall .= "$urlfilter";
				$apicall .= "&outputSelector[1]=SellerInfo&outputSelector[2]=PictureURLSuperSize&outputSelector[3]=StoreInfo&paginationInput.pageNumber=$pageNum";

				error_log("API CALL = ".$apicall, 0);

				// Load the call and capture the document returned by eBay API
				$resp = simplexml_load_file( $apicall);
				
				$outputJSON = array();
				$ack = $resp->ack;
				$resultCount = $resp->paginationOutput->totalEntries;
				$pageNumber = $resp->paginationOutput->pageNumber;
				$itemCount = $resp->paginationOutput->entriesPerPage;

				$outputJSON['ack'] = "$ack";
				$outputJSON['resultCount'] = "$resultCount";
				$outputJSON['pageNumber'] = "$pageNumber";
				$outputJSON['itemCount'] = "$itemCount";
				// Check to see if the request was successful, else print an error
				if ($ack == "Success") {
					$results = '';
					// If the response was loaded, parse it and build links
					if($resultCount == "0"){
						$outputJSON['ack'] = 'No results found';
					}
					else{
					
						$items = $resp->searchResult->item;
						$currentItem = 0;
						foreach($items as $item) {
							//$itm = array();
							$itemId = 'item'.$currentItem;

							// Basic info
							$title = $item->title;
							$viewItemURL  = $item->viewItemURL;
							$galleryURL   = $item->galleryURL;
							$pictureURLSuperSize = $item->pictureURLSuperSize;
							$convertedCurrentPrice = $item->sellingStatus->convertedCurrentPrice;
							$convertedCurrentPrice = "$convertedCurrentPrice";
							$shippingServiceCost = $item->shippingInfo->shippingServiceCost;
							$conditionDisplayName = $item->condition->conditionDisplayName;
							$listingType = $item->listingInfo->listingType;
							$location = $item->location;
							$categoryName = $item->primaryCategory->categoryName;
							$topRatedListing = $item->topRatedListing;
							//$basicInfo = array();
							$basicInfo['title'] = "$title";
							$basicInfo['viewItemURL'] = "$viewItemURL";
							$basicInfo['galleryURL'] = "$galleryURL";
							$basicInfo['pictureURLSuperSize'] = "$pictureURLSuperSize";
							$basicInfo['convertedCurrentPrice'] = "$convertedCurrentPrice";
							$basicInfo['shippingServiceCost'] = "$shippingServiceCost";
							$basicInfo['conditionDisplayName'] = "$conditionDisplayName";
							$basicInfo['listingType'] = "$listingType";
							$basicInfo['location'] = "$location";
							$basicInfo['categoryName'] = "$categoryName";
							$basicInfo['topRatedListing'] = "$topRatedListing";

							// Seller info
							$sellerUserName = $item->sellerInfo->sellerUserName;
							$feedbackScore = $item->sellerInfo->feedbackScore;
							$positiveFeedbackPercent = $item->sellerInfo->positiveFeedbackPercent;
							$feedbackRatingStar = $item->sellerInfo->feedbackRatingStar;
							$topRatedSeller = $item->sellerInfo->topRatedSeller;
							$sellerStoreName = $item->storeInfo->storeName;
							$sellerStoreURL = $item->storeInfo->storeURL;
							//$sellerInfo = array();
							$sellerInfo['sellerUserName'] = "$sellerUserName";
							$sellerInfo['feedbackScore'] = "$feedbackScore";
							$sellerInfo['positiveFeedbackPercent'] = "$positiveFeedbackPercent";
							$sellerInfo['feedbackRatingStar'] = "$feedbackRatingStar";
							$sellerInfo['topRatedSeller'] = "$topRatedSeller";
							$sellerInfo['sellerStoreName'] = "$sellerStoreName";
							$sellerInfo['sellerStoreURL'] = "$sellerStoreURL";
							
							//Shipping info
							$shippingType = $item->shippingInfo->shippingType;
							$shippingType = preg_replace_callback('/(?<!\b)[A-Z][a-z]+|(?<=[a-z])[A-Z]/', function($match) {
												return ' '. $match[0];
											}, $shippingType);
							$shippingType = $shippingType;
							$locationIndex = 0;
							$locations = $item->shippingInfo->shipToLocations;
							$shipToLocations = "";
							for($locationIndex=0;$locationIndex<count($locations);$locationIndex++){
								$shipToLocations = $shipToLocations.($locations[$locationIndex].",");
							}
							$shipToLocations = rtrim($shipToLocations, ",");
							$expeditedShipping = $item->shippingInfo->expeditedShipping;
							$oneDayShippingAvailable = $item->shippingInfo->oneDayShippingAvailable;
							$returnsAccepted = $item->returnsAccepted;
							$handlingTime = $item->shippingInfo->handlingTime; 
							//$shippingInfo = array();
							$shippingInfo['shippingType'] = "$shippingType";
							$shippingInfo['shipToLocations'] = "$shipToLocations";
							$shippingInfo['expeditedShipping'] = "$expeditedShipping";
							$shippingInfo['oneDayShippingAvailable'] = "$oneDayShippingAvailable";
							$shippingInfo['returnsAccepted'] = "$returnsAccepted";
							$shippingInfo['handlingTime'] = "$handlingTime";
					
							$itm['basicInfo'] = $basicInfo;
							$itm['sellerInfo'] = $sellerInfo;
							$itm['shippingInfo'] = $shippingInfo;
							$outputJSON[$itemId] = $itm;
							$currentItem++;
						}
					}
				}
				else{
					$outputJSON['ack'] = 'No results found';
				}
			}
	echo json_encode($outputJSON);
?>
var mainApp=angular.module('mainApp',['ngRoute']);
mainApp.config(['$routeProvider',function($routeProvider){
	$routeProvider.when('/',{
		controller: 'SimpleController',
		templateUrl: 'html/NewFile2.html'
	}).
	when('/app',{
		controller: 'SimpleController',
		templateUrl: 'html/BasicInfo.html'
	}).
	otherwise({rediectTo : '/'});
}]);
mainApp.factory('simpleFactory',function(){
	var factory ={};
	var customers =[
	                	{name:'vidya', city: 'banga'},
						{name:'anu', city:'kerala'}
	                ];
	factory.getCustomers=function(){
		 return customers;
	};
	return factory;
});
mainApp.controller('SimpleController',function($scope,$http,simpleFactory,$compile){
	$scope.selectedProject="";
	$scope.selectedVersion="";
	/*$scope.customers=[
	                  {name:'vidya', city: 'banga'},
	                  {name:'anu', city:'kerala'}
	                  ];
	*/
	$scope.customers=simpleFactory.getCustomers();
	$scope.addCustomer=function(){
		$scope.newCustomer.city ='default';
		$scope.customers.push(
				{
					name : $scope.newCustomer.name,
					city : $scope.newCustomer.city
				}
	)};
	$http({
		  method: 'GET',
		  url: 'rest/hello/init'
		}).then(function successCallback(response) {
		    // this callback will be called asynchronously
		    // when the response is available
			//alert("heeloo");
			$scope.projects=response.data;
			
		  }, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		  });
	
	$scope.getVersions=function(project){
		$http.get("rest/hello/getConfigs?project="+project).
		 then(function(response){
			 $scope.configVersions=response.data;
		 });
	};

	
	$scope.useVersion=function(){
		//console.log($scope.selectedVersion);
		console.log("sindes "+$scope.selectedProject);
		console.log("verson is "+$scope.selectedVersion);
		//console.log($scope.selectedProject);
		var url="rest/hello/useVersion?project="+$scope.selectedProject+"&version="+$scope.selectedVersion+"";
		console.log(url);
		$http.get(url).
		then(function(response){
			console.log(response.data);
			alert('Config was switched');
		});
		
	};
	$scope.setProject=function(param1){
		$scope.selectedProject=param1;
		console.log("param passed is "+param1);
	};
	$scope.testConfig=function(project){
		var restURL="rest/hello/testConfig?project="+project;
		console.log("ural passed is ..."+restURL);
		$http.get(restURL).
		 then(function(response){
			 console.log(response.data);
			 alert(response.data);
		 });
	};
	$scope.getConfigsFor=function(project){
		//alert(project);
		$scope.results=[];
		var divEle=project;
		for( var i in $scope.projects){
			//console.log(closeProject);
			if($scope.projects[i]!=project)
			  angular.element(document.getElementById($scope.projects[i])).html('');
		}
		var myElements= angular.element(document.getElementById(divEle));
		myElements.html('');
		var res="<li ng-mouseenter='setProject(\""+project+"\");' style='float:left;width:100%'>Config";
		res+="<div style='width:100%;'>";
		res+="<div style='float:left;width:40%'>";
		var restURL="rest/hello/upload?project="+project;
		res+="<form action='"+restURL+"' color='#FFFFFF' method='post' enctype='multipart/form-data' accept-charset='utf-8'>";
		res+="<input type='file' name='file' size='45'/>";
		res+="<input type='hidden' name='project' value='"+project+"'/>";
		res+="<input type='submit' value='Upload' ng-click='getVersions("+project+")'/>";
		res+="</form>";
		res+="</div>";
		//res+="</li>";
		console.log(myElements);
		
		res+="<div style='float:left'><select style='height:30px' ng-model='selectedVersion' ng-options='configVersion for configVersion in configVersions' ></select>";
		res+="<input type='button' ng-click='useVersion()' value='UseVersion'/>";
		res+="</div><input type='button' ng-click='testConfig(\""+project+"\")' value='test'></li>";
		var temp=$compile(res)($scope);
		myElements.append(temp);
		$scope.getVersions(project);
		
	};
	
	
});
























/*//Scroll Animation for About
$(function () {
	//caches a jQuery object containing the header element
	var animate = $("#progress");
	$(window).scroll(function () {
		var scroll = $(window).scrollTop();

		if (scroll >= 2900) {
			animate.removeClass('display-none').addClass("display");
		}
	});
});


//Scroll To
$(".scroll").click(function (event) {
	event.preventDefault();
	
	$("html,body").animate({scrollTop: $(this.hash).offset().top}, 400)
});


//Scroll Spy Refresh
$('#navbar').scrollspy()


//Scroll To Top
$(document).ready(function () {
	//Check to see if the window is top if not then display button
	$(window).scroll(function () {
		if ($(this).scrollTop() > 160) {
			$('.scrollToTop').fadeIn();
			
		} else {
			$('.scrollToTop').fadeOut();
		}
	});
	//Click event to scroll to top
	$('.scrollToTop').click(function () {
		$('html, body').animate({scrollTop: 0}, 800);
		return false;
	});
});


 function logohover()
 {
 var logo=document.getElementById("logo");
 logo.style.fontsize(20);
 alert(logo.style.fontSize);
 logo.style.fontFamily="Ariel";
 logo.innerHTML="Connecting Academia Product companies & Startups";
 }

 function logoout()
 {
 var logo=document.getElementById("logo");
 logo.style.fontsize(50);
 logo.innerHTML="CAPS";
 logo.style.fontFamily=" 'Lobster', cursive ";
 }
 */
document.addEventListener('DOMContentLoaded', init);

function init(){

    //get all links from DOM
    const links = document.getElementsByTagName('a'); 
    //create XML object for request
    const xhr = new XMLHttpRequest();
    //iterate over links
    for (let i = 0; i < links.length; i++) {
        
        let url = links[i].getAttribute('href');
        //check that link is not local and href attribute not null
        if (url && url.startsWith("http")) {
            
            request(xhr,url, links[i]);    
        }
        
    }



}

function request(xhr, url, link_element){

    try{    //try catch starts
        
        //open get req
        
        xhr.open('GET', url, true);
        //set function for onload request state
        xhr.onload = function() { manageResponse(this, link_element); };
        //send request
        
        xhr.send();
       

    }catch(error){
        console.log(error);
    }

    function manageResponse(e, link_element){
        //check if the server status is bad
        if(e.status > 399){
            link_element.setAttribute('href', '#');
            link_element.innerHTML = 'Broken link';
            console.log('risposta');
        }else{
            console.log("pa");
        }
    }

}
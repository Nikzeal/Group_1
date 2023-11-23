document.addEventListener('DOMContentLoaded', init);

function init(){

      
    //get all links from DOM
    const links = document.getElementsByTagName('a'); 
    
    //iterate over links collection
    for (let i = 0; i < links.length; i++) {
        
        let url = links[i].getAttribute('href');

        //check that link is not local and href attribute not null and ignore paths to same page or javascript
        if (url && !url.startsWith('#') && !url.startsWith("javascript")) {

            url.startsWith("http") ? exRequest(url, links[i]) : inRequest(url,links[i]);   
         
        }   
    }
}


function inRequest(url, link_element){
//no-cors mode for non http requests
fetch(url, {mode:'no-cors'})
    .then(function(response) {
         console.log(response.text());
    })
    .catch(function() {  
        console.log('non existing resource ' + url);
        link_element.setAttribute('href', '#');
        link_element.innerHTML = 'Broken link';
    });
    
 }


function exRequest(url, link_element){
    //jQuery request
    $.ajax({
        url: url ,
        type: 'HEAD',
        dataType: 'jsonp', 
        contentType: 'application/json',
        //fetch response when request has completed
        complete: function(xhr) {
            
            if (xhr.status >= 400) {
                link_element.setAttribute('href', '#');
                link_element.innerHTML = 'Broken link';
            }
            // else if (xhr.status == 200) { 
            //     console.log("good link");
            // }
        },
        error: function(xhr, textStatus, errorThrown){
            //console.log('err: ' + textStatus+ errorThrown);
        }
     });

}
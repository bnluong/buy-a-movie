/**
 *  TODO:
 */

/**
 * Handles the data returned by the API
 * @param resultData jsonObject
 */
function handleResult(resultData) {
    console.log("handleResult: handling resultData");
    handleRandomMovies(resultData)
}

/**
 * Handles random movies section
 * @param randomMovies jsonObject
 */
function handleRandomMovies(randomMovies) {
    console.log("handleRandomMovies: populating random movies");
    for (let i = 0; i < randomMovies.length; i++) {
        let movieImgID = '#movie' + (i + 1) + 'Img';
        let movieTitleID = '#movie' + (i + 1) + 'Title';
        let titleHTML = randomMovies[i]['title'] + ' (' + randomMovies[i]['year'] + ')';

        var imdbURL = 'https://www.imdb.com/title/' + randomMovies[i]['id'];
        $.ajax({
        	url: 'http://www.whateverorigin.org/get?url=' + encodeURIComponent(imdbURL) + '&callback=?',
        	dataType: 'json',
        	async: false,
        	success: function(data) {
        		var tempDOM = document.createElement('div');
        		tempDOM.innerHTML = data.contents;

        		var poster = tempDOM.getElementsByClassName('poster');
        		if (poster[0] != null) {
        			let tempDOM = document.createElement('div');
        			tempDOM.innerHTML = poster[0].innerHTML;
        			let posterURL = tempDOM.querySelector('img').getAttribute('src');
        			$(movieImgID).attr("src", posterURL);
        		} else {
        			$(movieImgID).attr("src", 'img/empty-box-art.png');
        		}
        	}
        });
        
        $(movieTitleID).html('<p class="h6">' + titleHTML);
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
// Makes the HTTP GET request and registers on success callback function handleResult

$.ajax("api/index", {
    method: "GET",
    success: handleResult
});
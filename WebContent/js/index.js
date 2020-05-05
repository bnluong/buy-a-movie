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
    for(let i = 0; i < randomMovies.length; i++) {
        let movieImgID = '#movie' + (i+1) + 'Img';
        var movieImgUrl = getMovieBoxArt(randomMovies[i]['title']);
        let movieTitleID = '#movie' + (i+1) + 'Title';
        let titleHTML = randomMovies[i]['title'] + ' (' + randomMovies[i]['year'] + ')';
        $(movieImgID).attr("src", movieImgUrl);
        console.log(titleHTML);
        $(movieTitleID).html('<p class="h6">' + titleHTML);
    }
}

function getMovieBoxArt(movieTitle) {
    var boxArtUrl = 'https://image.tmdb.org/t/p/w220_and_h330_face/h4VB6m0RwcicVEZvzftYZyKXs6K.jpg';

    return boxArtUrl;
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
// Makes the HTTP GET request and registers on success callback function handleResult
$.ajax("api/index", {
    method: "GET",
    success: handleResult
});
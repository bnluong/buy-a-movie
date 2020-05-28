export function getParameterByName(target) {
	// Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Use regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"), results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

export function parseGenresAsHTML(movieGenres) {
    var genresHTML = '';
    for(let i = 0; i < movieGenres.length; i++) {
        let genreName = movieGenres[i]['genre_name'];
        genresHTML += '<a href="browsing.html?' + '&genre=' + genreName + '&sortBy=rating&order=desc&numResults=10&offset=0' + '">' + genreName + '</a>'
        if(i != movieGenres.length - 1)
            genresHTML += ', ';
    }
    return genresHTML;
}

export function parseStarsAsHTML(movieStars) {
    var starsHTML = '';
    for(let i = 0; i < movieStars.length; i++) {
        let starID = movieStars[i]['star_id'];
        let starName = movieStars[i]['star_name'];
        starsHTML += '<a href="star.html?' + 'id=' + starID + '">' + starName + '</a>'
        if(i != movieStars.length - 1)
            starsHTML += ', ';
    }
    return starsHTML;
}
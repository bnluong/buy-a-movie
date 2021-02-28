import * as utils from './utilities.js';

function handleStarResult(resultData) {
    var starName = resultData['star_name']

    var starPortrait = resultData['star_portrait'];
    if(starPortrait == '')
        starPortrait = 'img/empty-portrait.png';

    var starProfession = resultData['star_profession'];
    if(starProfession == '')
    	starProfession = 'No information';
    		
    var starBirthYear = resultData['star_birthyear'];
    if(starBirthYear == null)
        starBirthYear = 'No information'

    var starBio = resultData['star_bio'];
    if(starBio == '')
    	starBio = 'No information';
    
    var starKnownFor = resultData['star_known_for'];
    var starFilmography = resultData['star_filmography'];

    $('#starTitle').text('Buy-a-Movie - ' + starName);

    $('#starName').append(
        $('<p>', { class: "h2" }).text(starName)
    );

    $('#starPortrait').append(
        $('<img>', {
            src: starPortrait,
            alt: starName + ' Portrait'
        })
    );

    $('#starProfession').append(
        $('<p>').append(
            $('<strong>').text('Profession: '),
            starProfession
        )
    );

    $('#starBirthyear').append(
        $('<p>').append(
            $('<strong>').text('Birthyear: '),
            starBirthYear
        )
    );

    $('#starBio').append(
        $('<p>').text(starBio)
    );

    for(let i = 0; i < starKnownFor.length; i++) {
        let movieID = starKnownFor[i]['movie_id'];
        let movieTitle = starKnownFor[i]['movie_title'];
        
        let moviePoster = starKnownFor[i]['movie_poster'];
        if(moviePoster == '')
            moviePoster = 'img/empty-poster.png';
        
        $('#starKnownFor').append(
            $('<div>', { class: 'col-xs-4 mr-3' }).append(
                $('<a>', { href: 'movie?id=' + movieID }).append(
                    $('<img>', {
                        src: moviePoster,
                        alt: movieTitle + ' Poster'
                    })
                ),
                $('<p>').append(
                    $('<a>', { href: 'movie.html?id=' + movieID }).text(movieTitle)
                )
            )
        );
    }

    for(let i = 0; i < starFilmography.length; i++) {
        let movieID = starFilmography[i]['movie_id'];
        let movieTitle = starFilmography[i]['movie_title'];
        let movieYear = starFilmography[i]['movie_year'];

        $('#filmographyTable').append(
            $('<tr>').append(
                $('<th>', { scope: 'row' }).text(movieYear),
                $('<td>').append(
                    $('<a>', { href: 'movie.html?id=' + movieID }).text(movieTitle)
                )
            )
        );
    }

}

const starID = utils.getParameterByName('id');

$.ajax({
    method: 'GET',
    url: 'api/star?id=' + starID,
    dataType: 'json',
    success: handleStarResult,
});
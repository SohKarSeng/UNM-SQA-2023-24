for (let i = 0; i < 12; i++) {
    //save note
    $('#saveNoteButton' + i).on('click', function () {
        let videoID = videoIDs[i];

        let time = convertTimeStampToSeconds(getVideoTimestamp(i));
        let note_key = generateNoteKey(videoID, time);

        saveNote(i, note_key);

        refreshDropdown(i);
    });

    $('#note-dropdown-' + i).on('change', function () {
        players[i].pause();
        $('#video-timestamp-' + i).html(convertSecondsToTimestamp($('#note-dropdown-' + i).val()));
        $('#video-timestamp-' + i).trigger('change');
    });

    $('#video-timestamp-' + i).on('change', function () {
        let timestamp = $('#video-timestamp-' + i).html();

        // look for the option where html is timestamp
        $('#note-dropdown-' + i + ' option').filter(function () {
            return $(this).html() == timestamp;
        }).prop('selected', true);

        let note_key = generateNoteKey(videoIDs[i], convertTimeStampToSeconds(timestamp));
        let note_content = localStorage.getItem(note_key);
        $('#note-content-' + i).val(note_content);
    });

    $('#note-content-' + i).on('focus', function () {
        players[i].pause();
    });

    $('#shareNoteButton-' + i).on('click', function () {
        let videoID = videoIDs[i];

        let time = convertTimeStampToSeconds(getVideoTimestamp(i));
        let note_key = generateNoteKey(videoID, time);

        shareNote(i, note_key);
    });

}

function defaultVideo() {
    let url = "https://www.googleapis.com/youtube/v3/search?key=" + API_KEY + "&type=video&maxResults=12&videoDuration=short&part=snippet&q=Software%20Quality%20Assurance";

    $.ajax({
        url: url,
        type: "GET",
        dataType: "json",
        success: function (data) {
            console.log(data)
            for (let i = 0; i < 12; i++) {
                videoIDs[i] = data.items[i].id.videoId;
                $('#videoPlayer' + i).attr('data-plyr-embed-id', videoIDs[i]);
                initializePlyr(i);

                $('#videoTitle' + i).html(data.items[i].snippet.title);
            }
        }
    });

}

function initializePlyr(playerID) {
    let intervals = [];

    players[playerID] = new Plyr('#videoPlayer' + playerID, {
        type: 'youtube'
    });

    players[playerID].on('playing', event => {
        // log the time every second
        intervals[playerID] = setInterval(() => {
            let time = players[playerID].currentTime.toFixed(0);
            $('#video-timestamp-' + playerID).html(convertSecondsToTimestamp(time));
            $('#video-timestamp-' + playerID).trigger('change');
        }, 1000);
    });

    players[playerID].on('pause', event => {
        // stop the interval
        console.log("pause")
        clearInterval(intervals[playerID]);
    });

    refreshDropdown(playerID);
}

function convertSecondsToTimestamp(seconds) {
    let minutes = Math.floor(seconds / 60);
    let secondsLeft = seconds % 60;
    if (secondsLeft < 10) {
        secondsLeft = "0" + secondsLeft;
    }
    return minutes + ":" + secondsLeft;
}

// Function to change YouTube video source
function changeVideo(playerID, videoID, videoTitle) {
    // Destroy the existing player
    players[playerID].destroy();

    $('#videoPlayer' + playerID).attr('data-plyr-embed-id', videoID);
    initializePlyr(playerID);

    // Change the title
    $('#videoTitle' + playerID).html(videoTitle);
}

function pauseUserInput(time) {
    $('#section-block').block({
        message: '<div class="spinner-border text-primary" role="status"></div>',
        timeout: time,
        css: {
            backgroundColor: 'transparent',
            border: '0'
        },
        overlayCSS: {
            backgroundColor: '#fff',
            opacity: 0.8
        }
    });
}

function getFinalQuery() {
    let query = "";

    let keyword = $('#keyword-search').val();
    let keywordsCategory = $('#category-select').val();

    for (const keyword in keywordsCategory) {
        keywordsCategory[keyword] = keywordsCategory[keyword].replace(/ /g, "%20")
        query += keywordsCategory[keyword] + "%2C";
    }
    return (query += keyword);
}

function getVideoTimestamp(key) {
    return $('#video-timestamp-' + key).html();
}

function generateNoteKey(videoID, time) {
    return videoID + "___" + time;
}

function convertTimeStampToSeconds(timestamp) {
    let minutes = timestamp.split(":")[0];
    let seconds = timestamp.split(":")[1];
    return parseInt(minutes) * 60 + parseInt(seconds);
}

function saveNote(key, note_key) {
    let note_content = $('#note-content-' + key).val();
    console.log("Note content: " + note_content);
    localStorage.setItem(note_key, note_content);
}

function refreshDropdown(key) {
    let note_dropdown = $('#note-dropdown-' + key);
    note_dropdown.empty();
    note_dropdown.append('<option value="">Select a note</option>');

    let note_keys = [];
    for (let i = 0; i < localStorage.length; i++) {
        let note_key = localStorage.key(i);
        if (note_key.includes(videoIDs[key])) {
            note_keys.push(note_key);
        }
    }

    note_keys.sort();

    for (let i = 0; i < note_keys.length; i++) {
        let note_key = note_keys[i];
        let note_timestamp = note_key.split("___")[1];
        note_dropdown.append('<option value="' + note_timestamp + '">' + convertSecondsToTimestamp(note_timestamp) + '</option>');
    }
}

function searchVideo() {
    pauseUserInput(5000);

    let url = "https://www.googleapis.com/youtube/v3/search?key=" + API_KEY + "&type=video&maxResults=12&videoDuration=short&part=snippet&q=";

    let final_query = getFinalQuery();

    if (final_query != "") {
        url += final_query;
    } else {
        url += "Software%20Quality%20Assurance";
    }

    // get the videos
    $.ajax({
        url: url,
        type: "GET",
        dataType: "json",
        success: function (data) {
            for (let i = 0; i < 12; i++) {
                videoIDs[i] = data.items[i].id.videoId;
                changeVideo(i, data.items[i].id.videoId, data.items[i].snippet.title);
                $('#video-timestamp-' + i).html("0:00");
                $('#note-content-' + i).val("");
            }
        }
    });
}

// share note in clipboard with video url
function shareNote(key, note_key) {
    let note_content = $('#note-content-' + key).val();
    let video_url = "https://www.youtube.com/watch?v=" + videoIDs[key];
    let note_url = video_url + "&note=" + note_key;
    let note_timestamp = convertSecondsToTimestamp(note_key.split("___")[1]);

    navigator.clipboard.writeText("Video URL : " + note_url + "\n\n" + "Note Content : " + note_timestamp + " " + note_content).then(function () {
        console.log('Async: Copying to clipboard was successful!');
    }, function (err) {
        console.error('Async: Could not copy text: ', err);
    });
}

var fileName = process.argv[2];

function write(start, end) {
    if (start !== end) {
        var tmp = start + parseInt((end-start)/2);
        console.log(arr[tmp]);
        write(start, tmp);
        write(tmp +1, end);
    }
}

var fs = require('fs');
let arr = [];

var text = fs.readFileSync(fileName, "utf8");
text.split(/[*()\- \",.;:\/\n\r]/).forEach(function (line) {
    if (line.length > 0) {
        arr.push(line);
    }
});


write(0,arr.length);
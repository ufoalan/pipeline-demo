const fs = require("fs");
const https = require("https");
const options = {
  key: fs.readFileSync(`${__dirname}/certs/server/tls.key`),
  cert: fs.readFileSync(`${__dirname}/certs/server/tls.crt`),
  ca: [
    fs.readFileSync(`${__dirname}/certs/ca/tls.crt`)
  ],
  // Requesting the client to provide a certificate, to authenticate.
  requestCert: true,
  // As specified as "true", so no unauthenticated traffic
  // will make it to the specified route specified
  //rejectUnauthorized: true
  rejectUnauthorized: false
};
https
  .createServer(options, function(req, res) {
    console.log(
      new Date() +
        " " +
        req.connection.remoteAddress +
        " " +
        req.method +
        " " +
        req.url
    );
    res.writeHead(200);
    res.end("OK!\n");
  })
  .listen(8080);

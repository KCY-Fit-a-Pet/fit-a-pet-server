function main(params) {
    const AWS = require("aws-sdk");
    const { v4: uuidv4 } = require('uuid');

    const endpoint = new AWS.Endpoint("https://kr.object.ncloudstorage.com");
    const region = "kr-standard";
    const access_key = params.access;
    const secret_key = params.secret;

    const S3 = new AWS.S3({
        endpoint: endpoint,
        region: region,
        credentials: {
            accessKeyId: access_key,
            secretAccessKey: secret_key,
        },
        signatureVersion: 'v4'
    });

    const bucket_name = "pkcy";
    const object_name = params.dirname + "/" + uuidv4().substring(0, 13).replace("-", "") + "." + params.extension;
    const signedUrlExpireSeconds = 300;
    // const content_type = "image/*"

    const url = S3.getSignedUrl("putObject", {
        Bucket: bucket_name,
        Key: object_name,
        Expires: signedUrlExpireSeconds,
        ACL:'public-read'
    });

    console.log(url);
    // presigned url
    return {payload: url};
}
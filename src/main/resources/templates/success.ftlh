<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;500;700&display=swap" rel="stylesheet">
    <title>Thank You!</title>
    <style>
        :root {
            --successColor: #4caf50;
            --backgroundColor: #fafafa;
            --textColor: #2d2d2d;
            --secondaryTextColor: #555555;
            --linkColor: #4caf50;
            --borderColor: #dedede;
        }

        * {
            font-family: 'Montserrat', sans-serif;
            box-sizing: border-box;
        }

        body {
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: var(--backgroundColor);
            margin: 0;
            height: 100vh;
        }

        .message-box {
            max-width: 480px;
            background-color: #ffffff;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0px 10px 20px rgba(0, 0, 0, 0.1);
            text-align: center;
            border: 1px solid var(--borderColor);
        }

        .message-box .icon {
            font-size: 60px;
            color: var(--successColor);
            margin-bottom: 20px;
        }

        .message-box h1 {
            font-size: 24px;
            color: var(--textColor);
            margin-bottom: 10px;
        }

        .message-box p {
            color: var(--secondaryTextColor);
            font-size: 16px;
            line-height: 1.5;
            margin-bottom: 20px;
        }

        .message-box .contact {
            color: var(--secondaryTextColor);
            font-size: 15px;
        }

        .message-box .contact a {
            color: var(--linkColor);
            text-decoration: none;
            font-weight: 500;
        }

        .message-box .contact a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="message-box">
    <div class="icon">&#10004;</div>
    <h1>Thank You for Your Support!</h1>
    <p>
        We appreciate your trust in us. Your satisfaction is our priority, and we’re here to provide the best experience possible.
    </p>
    <div class="contact">
        Reach out at <a href="mailto:support@yourcompany.com">support@yourcompany.com</a> or visit our
        <a href="https://yourcompany.com/help">Help Center</a> for any assistance.
    </div>
</div>

</body>
</html>

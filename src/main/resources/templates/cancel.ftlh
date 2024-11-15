<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;500;700&display=swap"
          rel="stylesheet">
    <title>Payment Failed</title>
    <style>
        :root {
            --errorColor: #ff6b6b;
            --backgroundColor: #fafafa;
            --primaryTextColor: #2d2d2d;
            --secondaryTextColor: #555555;
            --buttonBackground: #4a90e2;
            --buttonHoverBackground: #357abd;
            --buttonTextColor: #ffffff;
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
            color: var(--errorColor);
            margin-bottom: 20px;
        }

        .message-box h1 {
            font-size: 24px;
            color: var(--primaryTextColor);
            margin-bottom: 10px;
        }

        .message-box p {
            color: var(--secondaryTextColor);
            font-size: 16px;
            line-height: 1.5;
            margin-bottom: 20px;
        }

        .message-box .actions a {
            display: inline-block;
            padding: 12px 25px;
            color: var(--buttonTextColor);
            background-color: var(--buttonBackground);
            border-radius: 6px;
            text-decoration: none;
            font-weight: 500;
            transition: background-color 0.3s ease;
            margin: 0 5px;
        }

        .message-box .actions a:hover {
            background-color: var(--buttonHoverBackground);
        }
    </style>
</head>
<body>

<div class="message-box">
    <div class="icon">&#10060;</div>
    <h1>Payment Unsuccessful</h1>
    <p>
        Your payment was not completed. You can retry or contact support for further assistance.
    </p>
    <div class="actions">
        <a>Retry Payment</a>
        <a>Contact Support</a>
    </div>
</div>

</body>
</html>

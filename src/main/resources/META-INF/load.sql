INSERT INTO AbstractOutputHandlerConfiguration SET DTYPE = 'SYSOUT', prefix = 'Template Processing Result: [', suffix = ']';
INSERT INTO AbstractOutputHandlerConfiguration SET DTYPE = 'EMAIL', smtpServer = 'smtp.gmail.com', port = 587, authorization = 1, username = 'test', password = 'test', security = 'TLS', contentType = 'text/plain';
INSERT INTO AbstractOutputHandlerConfiguration SET DTYPE = 'EMAIL', smtpServer = 'smtp.gmail.com', port = 465, authorization = 1, username = 'test', password = 'test', security = 'SSL', contentType = 'text/html';

INSERT INTO Channel SET name = 'email_html', outputHandler = 'EMAIL', config_id = 3;
INSERT INTO Channel SET name = 'email_text', outputHandler = 'EMAIL', config_id = 2;
INSERT INTO Channel SET name = 'sms', outputHandler = 'SYSOUT', config_id = 1;

INSERT INTO Template SET name = 'DOI';

INSERT INTO ChannelTemplate SET template_id = 1, channel_id = 1, subject = '${firstname}, please confirm your registration', content = '<html><body>Hello ${firstname} ${lastname},<br /><br />thanks for signing up!<br /><br /><strong>In order to activate your account, please click the following link:</strong><br /><br /><a href="http://localhost/activate?code=${code}">Click here</a><#if includeDisclaimer><br /><br />If you did not sign up, please just ignore this mail</#if><br /><br />Kind regards,<br />Your company</html></body>';
INSERT INTO ChannelTemplate SET template_id = 1, channel_id = 2, subject = '${firstname}, please confirm your registration', content = 'Hello ${firstname} ${lastname},\n\nthanks for signing up!\n\nIn order to activate your account, please click the following link:\n\nhttp://localhost/activate?code=${code}<#if includeDisclaimer>\n\nIf you did not sign up, please just ignore this mail</#if>\n\nKind regards,\nYour company';
INSERT INTO ChannelTemplate SET template_id = 1, channel_id = 3, subject = '', content = 'Hello ${firstname}, for activating your account, please use the following code: ${code}\nThank you!';

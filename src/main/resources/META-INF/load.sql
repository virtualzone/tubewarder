INSERT INTO Channel SET name = 'email_html';
INSERT INTO Channel SET name = 'email_text';
INSERT INTO Channel SET name = 'sms';

INSERT INTO Template SET name = 'DOI';

INSERT INTO ChannelTemplate SET template_id = 1, channel_id = 3, content = 'Hello, for activating your account, please use the following code: ${code}\nThank you!';
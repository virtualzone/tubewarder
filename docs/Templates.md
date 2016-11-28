# Creating Templates
This chapter is meant to provide some useful examples of how to create templates.

To find out more about the Syntax you can use in your templates, please have a look at the [Handlebars](http://handlebarsjs.com/) website.

## A simple template
We're setting three static variables here: firstname, lastname and code. All three parameters must be provided when calling the Send API. Otherwise, the rendering will fail and the Send API will return error code 6 (missing template parameter).

__Template__:
```
Hello {{firstname}} {{lastname}},

thanks for signing up! Please click this link to activate your account:

http://localhost/confirm?c={{code}}
```

__JSON Model provided with Send API__: 
```
{
    "firstname": "John",
    "lastname": "Doe",
    "code": "1234567890"
}
```

## A conditional template
This is a conditional template with an IF statement. The firstname and lastname variables are mandatory again. However, the password variable is only required if isNewCustomer variable is set. Otherwise, some static text is gets included.

__Template__:
```
Hello {{firstname}} {{lastname}},

thanks for your order!
You can see the status of your order on our website.

{{#if isNewCustomer}}
    Log in with this password we've generated for you: {{password}}
{{else}}
    Please log in with your known password.
{{/if}}
```

__JSON Model provided with Send API__: 
```
{
    "firstname": "John",
    "lastname": "Doe",
    "isNewCustomer": true,
    "password": "1234567890"
}
```

## A list template
In this template, a list of items is printed.

__Template__:
```
Hello {{firstname}} {{lastname}},

here is a list of your ordered items:

{{#each items}}
 - {{product}} at {{price}} EUR
{{/each}}
```

__JSON Model provided with Send API__: 
```
{
    "firstname": "John",
    "lastname": "Doe",
    "items": [
        {"product": "Computer", "price": 999.99},
        {"product": "Desk", "price": 250}
    ]
}
```

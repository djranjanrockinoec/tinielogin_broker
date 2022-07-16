package com.tinie.login_broker.util;

public final class Constants {
    public static final long MILLIS_DAY = 86_400_000;
    public static String WHATSAPP_REQUEST_BODY = """
            {
                "messaging_product": "whatsapp",
                "to": "%s",
                "type": "template",
                "template": {
                    "name": "%s",
                    "language": {
                        "code": "en_US"
                    },
                    "components": [
                        {
                            "type": "body",
                            "parameters": [
                                {
                                    "type": "text",
                                    "text": "%d"
                                }
                            ]
                        }
                    ]
                }
            }
            """;
}

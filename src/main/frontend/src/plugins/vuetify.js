import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.min.css'

import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

export default createVuetify({
    components,
    directives,
    theme: {
        defaultTheme: 'dark',
        themes: {
            dark: {
                colors: {
                    background: '#352945',
                    surface: '#3f3951',
                    primary: '#3b82f6',
                    secondary: '#8b5cf6',
                    error: '#ef4444',
                    success: '#10b981',
                    warning: '#f59e0b', 
                }
            }
        }
    },
})
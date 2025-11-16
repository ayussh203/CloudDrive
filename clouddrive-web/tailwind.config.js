// tailwind.config.js
export default {
  content: ['./index.html', './src/**/*.{ts,tsx,js,jsx}'],
  theme: {
    extend: {
      colors: {
        brand: {
          50:'#f4fbff',100:'#e6f6ff',200:'#c7ebff',300:'#a3deff',
          400:'#6ac8ff',500:'#2fb2ff',600:'#1496e6',700:'#0e77b4',
          800:'#0c5f90',900:'#0a4f77'
        }
      },
      boxShadow: { soft: '0 10px 30px rgba(2,12,27,.08)' },
      backdropBlur: { xs: '2px' },
    },
  },
  plugins: [],
};

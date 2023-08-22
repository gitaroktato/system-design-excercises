import React from 'react';
import logo from '../logo.svg'
import { Box } from '@mui/material';

function Logo() {
  return (
    <Box
        sx={{
        height: 400,
        width: 600,
        maxHeight: { xs: 150, md: 250, lg: 400 },
        maxWidth: { xs: 200, md: 300, lg: 600 },
        }}
        component="img"
        alt="Logo"
        src={logo}
    />
  )
}

export default Logo;
import React from 'react';
import Search from './search/Search';
import Logo from './logo/Logo';
import { Grid } from '@mui/material';

function App() {
  return (
    <Grid
      container
      spacing={0}
      direction="column"
      alignItems="center"
      justifyContent="center"
    >
      <Grid item>
          <Logo />
          <Search />
      </Grid>
    </Grid>
  );
}

export default App;

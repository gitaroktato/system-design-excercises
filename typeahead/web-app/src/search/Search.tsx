import React from 'react';
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';

function Search() {
  return (
    <Autocomplete
        freeSolo
        id = "search-field"
        disableClearable
        options = {[]}
        filterOptions={(x) => x}
        renderInput = {(params) => (
          <TextField
            {...params}
            label="Search"
            InputProps={{
              ...params.InputProps,
              type: 'search',
            }}
          />
        )}
      />
  )
}

export default Search;
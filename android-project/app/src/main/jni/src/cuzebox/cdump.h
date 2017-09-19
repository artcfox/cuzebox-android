/*
 *  CROM dump functions
 *
 *  Copyright (C) 2016
 *    Sandor Zsuga (Jubatian)
 *  Uzem (the base of CUzeBox) is copyright (C)
 *    David Etherton,
 *    Eric Anderton,
 *    Alec Bourque (Uze),
 *    Filipe Rinaldi,
 *    Sandor Zsuga (Jubatian),
 *    Matt Pandina (Artcfox)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/



#ifndef CDUMP_H
#define CDUMP_H



#include "types.h"



/*
** Note: Later this might be expanded to support setting paths and other
** necessities for the CROM dumps.
*/



/*
** Tries to load CROM state from an crom dump. If the CROM dump does not
** exist, it clears the CROM.
*/
void cdump_load(uint8* crom);


/*
** Tries to write out CROM state into an crom dump. It fails silently if
** this is not possible.
*/
void cdump_save(uint8 const* crom);


#endif

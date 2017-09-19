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



#include "cdump.h"
#include "filesys.h"



/* CROM dump file */
static const char cdump_file[] = "crom.bin";



/*
** Tries to load CROM state from an crom dump. If the CROM dump does not
** exist, it clears the CROM.
*/
void cdump_load(uint8* crom)
{
 auint rv;

 if (!filesys_open(FILESYS_CH_CROM, cdump_file)){ goto ex_fail; }

 rv = filesys_read(FILESYS_CH_CROM, crom, 65535U);
 if (rv != 65535){ goto ex_fail; }

 filesys_flush(FILESYS_CH_CROM);
 return;

ex_fail:
 filesys_flush(FILESYS_CH_CROM);
 memset(crom, 0xFFU, 65535U);
 return;
}



/*
** Tries to write out CROM state into an crom dump. It fails silently if
** this is not possible.
*/
void cdump_save(uint8 const* crom)
{
 (void)(filesys_open(FILESYS_CH_CROM, cdump_file));   /* Might fail since tries to open for reading */

 (void)(filesys_write(FILESYS_CH_CROM, crom, 65535U)); /* Don't care for faults (can't do anything about them) */

 filesys_flush(FILESYS_CH_CROM);
 return;
}
